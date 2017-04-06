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

classification_glmnb <- function (train, test) 
{  
  model.glm.nb <- glm.nb(MQnextRelease ~ MQ, data=train)	
  test.prob <- predict(model.glm.nb, test, type="response")		
  
  pred <- prediction(test.prob, test$MQnextRelease>0.2)
  auc <- performance(pred,"auc")@y.values[[1]]
  
  #return(list(auc=auc))
  print(paste0("N-AUC:", auc))
  
}

ranking_glmnb <- function (train, test) 
{
  model.glm.nb <- glm.nb(MQnextRelease ~ MQ, data=train)
  test.pred <- predict(model.glm.nb, test, type="response")
  spearman <- cor(test$MQnextRelease, test.pred, method="spearman")
  spearman.p <- cor.test(test$MQnextRelease, test.pred, method="spearman", exact=FALSE)$p.value
  #return(list(spearman=spearman, spearman.p=spearman.p))
  print(paste0("N-spearman: ", spearman, " N-spearman.p: ", spearman.p))
  
}

classification_linear <- function (train, test) 
{
  model.lm <- lm(MQnextRelease ~ MQ, data=train)
  test.prob <- predict(model.lm, test, type="response")
  pred <- prediction(test.prob, test$MQnextRelease>0.2)
  auc <- performance(pred,"auc")@y.values[[1]]
  print(paste0("L-AUC:", auc))	
}

ranking_linear <- function (train, test) 
{
  model.lm <- lm(MQnextRelease ~ MQ, data=train)
  test.pred <- predict(model.lm, test)
  spearman <- cor(test$MQnextRelease, test.pred, method="spearman")
  spearman.p <- cor.test(test$MQnextRelease, test.pred, method="spearman", exact=FALSE)$p.value
  print(paste0("L-spearman: ", spearman, " L-spearman.p: ", spearman.p))
}


classification_randomForest <- function (train, test) 
{
  randomForest <- randomForest(MQnextRelease ~ MQ, data= train)
  test.prob <- predict(randomForest, test, type="response")
  pred <- prediction(test.prob, test$MQnextRelease>0.2)
  auc <- performance(pred,"auc")@y.values[[1]]
  print(paste0("F-AUC:", auc))	
  
}

ranking_randomForest <- function (train, test) 
{
  randomForest <- randomForest(MQnextRelease ~ MQ, data= train)
  test.pred <- predict(randomForest, test, type="response")
  spearman <- cor(test$MQnextRelease, test.pred, method="spearman")
  spearman.p <- cor.test(test$MQnextRelease, test.pred, method="spearman", exact=FALSE)$p.value
  print(paste0("F-spearman: ", spearman, " F-spearman.p: ", spearman.p))
}


classification_glmnb(TrainingData, TestData)
classification_linear(TrainingData, TestData)
classification_randomForest(TrainingData, TestData)

ranking_glmnb(TrainingData, TestData)
ranking_linear(TrainingData, TestData)
ranking_randomForest(TrainingData, TestData)
