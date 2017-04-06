require(MASS) #glm.nb
require(ROCR)
require(randomForest)

options(digits=2)
# this is wrapped in a tryCatch. The first expression works when source executes, the
# second expression works when R CMD does it.
full.fpath <- tryCatch(normalizePath(parent.frame(2)$ofile),  # works when using source
                       error=function(e) # works when using R CMD
                         normalizePath(unlist(strsplit(commandArgs()[grep('^--file=', commandArgs())], '='))[2]))
script.dir <- dirname(full.fpath)
TrainingData<-read.csv(file.path(getwd(),"TrainingData.csv"),header=T)
TestData<-read.csv(file.path(getwd(),"TestData.csv"),header=T)

sampleVec <- vector(mode="numeric", length = nrow(TrainingData))
for (i in 1:nrow(TrainingData))
{			
  if (TrainingData[i,"SPF"] == 0 & TrainingData[i,"SPFnextRelease"] == 1)
    sampleVec[i] <- 1
  
}	
TrainingData$newSPF <- sampleVec
#summary(TrainingData$newSPF)
length(which(TrainingData$newSPF == 1))
length(which(TrainingData$newSPF == 1))/nrow(TrainingData)


sampleVec <- vector(mode="numeric", length = nrow(TestData))
for (i in 1:nrow(TestData))
{			
  if (TestData[i,"SPF"] == 0 & TestData[i,"SPFnextRelease"] == 1)
    sampleVec[i] <- 1
  
}	
TestData$newSPF <- sampleVec
#summary(TestData$newSPF)
length(which(TestData$newSPF == 1))
length(which(TestData$newSPF == 1))/nrow(TestData)

classification_glmnb <- function (train, test) 
{  
  model.glm.nb <- glm.nb(newSPF ~ SPF, data=train)	
  test.prob <- predict(model.glm.nb, test, type="response")		
  
  pred <- prediction(test.prob, test$newSPF>0)
  auc <- performance(pred,"auc")@y.values[[1]]
  
  #return(list(auc=auc))
  print(paste0("N-AUC:", auc))
  
}

classification_linear <- function (train, test) 
{
  model.lm <- lm(newSPF ~ SPF, data=train)
  test.prob <- predict(model.lm, test, type="response")
  pred <- prediction(test.prob, test$newSPF>0)
  auc <- performance(pred,"auc")@y.values[[1]]
  print(paste0("L-AUC:", auc))	
}

classification_randomForest <- function (train, test) 
{
  randomForest <- randomForest(newSPF ~ SPF, data= train)
  test.prob <- predict(randomForest, test, type="response")
  pred <- prediction(test.prob, test$newSPF>0)
  auc <- performance(pred,"auc")@y.values[[1]]
  print(paste0("F-AUC:", auc))	
  
}

classification_glmnb(TrainingData, TestData)
classification_linear(TrainingData, TestData)
classification_randomForest(TrainingData, TestData)



















