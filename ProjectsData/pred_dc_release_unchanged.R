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

classification_unchanged <- function (train, test) 
{
  model.lm <- lm(BDCnextRelease ~ BDC, data=train)
  test.prob <- predict(model.lm, test, type="response")
  pred <- prediction(test.prob, test$BDCnextRelease>0)
  auc <- performance(pred,"auc")@y.values[[1]]
  print(paste0("L-AUC:", auc))	
}

classification_unchanged(TrainingData, TestData)
