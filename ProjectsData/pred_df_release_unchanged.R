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

classification_nb_unchanged <- function (train, test) 
{
  model.glm.nb <- glm.nb(BugCountMult ~ BugCountMult, data=train)	
  test.prob <- predict(model.glm.nb, test, type="response")		
  
  pred <- prediction(test.prob, test$BugCountMult>0)
  auc <- performance(pred,"auc")@y.values[[1]]
  
  #return(list(auc=auc))
  print(paste0("N-AUC:", auc))
}

ranking_nb_unchanged <- function (train, test) 
{
  model.glm.nb <- glm.nb(BugCountMult ~ BugCountMult, data=train)
  test.pred <- predict(model.glm.nb, test, type="response")
  spearman <- cor(test$BugCountMult, test.pred, method="spearman")
  spearman.p <- cor.test(test$BugCountMult, test.pred, method="spearman", exact=FALSE)$p.value
  #return(list(spearman=spearman, spearman.p=spearman.p))
  print(paste0("N-spearman: ", spearman, " N-spearman.p: ", spearman.p))
}

classification_linear_unchanged <- function (train, test) 
{
  model.lm <- lm(BugCountMult ~ BugCountMult, data=train)
  test.prob <- predict(model.lm, test, type="response")
  pred <- prediction(test.prob, test$BugCountMult>0)
  auc <- performance(pred,"auc")@y.values[[1]]
  print(paste0("L-AUC:", auc))	
}

ranking_linear_unchanged <- function (train, test) 
{
  model.lm <- lm(BugCountMult ~ BugCountMult, data=train)
  test.pred <- predict(model.lm, test)
  spearman <- cor(test$BugCountMult, test.pred, method="spearman")
  spearman.p <- cor.test(test$BugCountMult, test.pred, method="spearman", exact=FALSE)$p.value
  print(paste0("L-spearman: ", spearman, " L-spearman.p: ", spearman.p))
}


classification_nb_unchanged(TrainingData, TestData)
ranking_nb_unchanged(TrainingData, TestData)

classification_linear_unchanged(TrainingData, TestData)
ranking_linear_unchanged(TrainingData, TestData)











