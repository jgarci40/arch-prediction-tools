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
  if (TrainingData[i,"BUO"] == 0 & TrainingData[i,"BUOnextRelease"] == 1)
    sampleVec[i] <- 1
  
}	
TrainingData$newBUO <- sampleVec
#summary(TrainingData$newBUO)
length(which(TrainingData$newBUO == 1))
length(which(TrainingData$newBUO == 1))/nrow(TrainingData)


sampleVec <- vector(mode="numeric", length = nrow(TestData))
for (i in 1:nrow(TestData))
{			
  if (TestData[i,"BUO"] == 0 & TestData[i,"BUOnextRelease"] == 1)
    sampleVec[i] <- 1
  
}	
TestData$newBUO <- sampleVec
#summary(TestData$newBUO)
length(which(TestData$newBUO == 1))
length(which(TestData$newBUO == 1))/nrow(TestData)

classification_glmnb <- function (train, test) 
{  
  model.glm.nb <- glm.nb(newBUO ~ BUO, data=train)	
  test.prob <- predict(model.glm.nb, test, type="response")		
  
  pred <- prediction(test.prob, test$newBUO>0)
  auc <- performance(pred,"auc")@y.values[[1]]
  
  #return(list(auc=auc))
  print(paste0("N-AUC:", auc))
  
}

classification_linear <- function (train, test) 
{
  model.lm <- lm(newBUO ~ BUO, data=train)
  test.prob <- predict(model.lm, test, type="response")
  pred <- prediction(test.prob, test$newBUO>0)
  auc <- performance(pred,"auc")@y.values[[1]]
  print(paste0("L-AUC:", auc))	
}

classification_randomForest <- function (train, test) 
{
  randomForest <- randomForest(newBUO ~ BUO, data= train)
  test.prob <- predict(randomForest, test, type="response")
  pred <- prediction(test.prob, test$newBUO>0)
  auc <- performance(pred,"auc")@y.values[[1]]
  print(paste0("F-AUC:", auc))	
  
}

classification_glmnb(TrainingData, TestData)
classification_linear(TrainingData, TestData)
classification_randomForest(TrainingData, TestData)








