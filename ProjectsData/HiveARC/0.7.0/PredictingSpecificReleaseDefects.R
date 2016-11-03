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
TrainingData<-read.csv(file.path(script.dir,"TrainingData.csv"),header=T)
TestData<-read.csv(file.path(script.dir,"TestData.csv"),header=T)


summary(m1 <- glm.nb(BugCountMult ~ log2(LOC+1) + log2(numberOfCommits+1) + log2(CountClassCoupled+1) + log2(MaxInheritanceTree+1) + log2(PercentLackOfCohesion+1) + log2(SumCyclomatic+1) +log2(coChangedDifferentPackage+1)+log2(coChangedSamePackage+1)+ log2(NumCochangedFiles+1) + BCO + SPF + BDC + BUO +log2(incomingDep+1) + log2(outgoingDep+1) + log2(internalEdges +1) + log2(externalEdges +1) + log2(edgesInto+1) + log2(edgesOutOf+1), data= TrainingData))

step <- stepAIC(m1, direction="both")
step$anova # display results

classification_glmnb <- function (train, test) 
{  
	model.glm.nb <- glm.nb(BugCountMult ~ log2(CountClassCoupled + 1) + log2(MaxInheritanceTree + 
    1) + log2(coChangedDifferentPackage + 1) + log2(coChangedSamePackage + 
    1) + BCO + BDC + log2(externalEdges + 1), data=train)	
	test.prob <- predict(model.glm.nb, test, type="response")		
	
	pred <- prediction(test.prob, test$BugCountMult>0)
	auc <- performance(pred,"auc")@y.values[[1]]
	
	#return(list(auc=auc))
	print(paste0("N-AUC:", auc))
	
}

ranking_glmnb <- function (train, test) 
{
	model.glm.nb <- glm.nb(BugCountMult ~ log2(CountClassCoupled + 1) + log2(MaxInheritanceTree + 
    1) + log2(coChangedDifferentPackage + 1) + log2(coChangedSamePackage + 
    1) + BCO + BDC + log2(externalEdges + 1), data=train)
	test.pred <- predict(model.glm.nb, test, type="response")
	spearman <- cor(test$BugCountMult, test.pred, method="spearman")
	spearman.p <- cor.test(test$BugCountMult, test.pred, method="spearman", exact=FALSE)$p.value
	#return(list(spearman=spearman, spearman.p=spearman.p))
	print(paste0("N-spearman: ", spearman, " N-spearman.p: ", spearman.p))
	
}


summary(m1 <- lm(BugCountMult ~ LOC + numberOfCommits + CountClassCoupled + MaxInheritanceTree + PercentLackOfCohesion + SumCyclomatic + NumCochangedFiles + coChangedDifferentPackage + coChangedSamePackage + BCO + SPF + BDC + BUO + incomingDep + outgoingDep + internalEdges + externalEdges + edgesInto + edgesOutOf, data= TrainingData))

step <- stepAIC(m1, direction="both")
step$anova # display results


classification_linear <- function (train, test) 
{
	model.lm <- lm(BugCountMult ~ numberOfCommits + CountClassCoupled + MaxInheritanceTree + 
    SumCyclomatic + NumCochangedFiles + coChangedSamePackage + 
    BCO + BDC + incomingDep + outgoingDep + externalEdges, data=train)
	test.prob <- predict(model.lm, test, type="response")
	pred <- prediction(test.prob, test$BugCountMult>0)
	auc <- performance(pred,"auc")@y.values[[1]]
	print(paste0("L-AUC:", auc))	
}

ranking_linear <- function (train, test) 
{
	model.lm <- lm(BugCountMult ~ numberOfCommits + CountClassCoupled + MaxInheritanceTree + 
    SumCyclomatic + NumCochangedFiles + coChangedSamePackage + 
    BCO + BDC + incomingDep + outgoingDep + externalEdges, data=train)
	test.pred <- predict(model.lm, test)
	spearman <- cor(test$BugCountMult, test.pred, method="spearman")
	spearman.p <- cor.test(test$BugCountMult, test.pred, method="spearman", exact=FALSE)$p.value
	print(paste0("L-spearman: ", spearman, " L-spearman.p: ", spearman.p))
}



classification_randomForest <- function (train, test) 
{
	randomForest <- randomForest(BugCountMult ~ LOC + numberOfCommits + CountClassCoupled + MaxInheritanceTree + PercentLackOfCohesion + SumCyclomatic + NumCochangedFiles + coChangedDifferentPackage + coChangedSamePackage + BCO + SPF + BDC + BUO + incomingDep + outgoingDep + internalEdges + externalEdges + edgesInto + edgesOutOf, data= train)
	test.prob <- predict(randomForest, test, type="response")
	pred <- prediction(test.prob, test$BugCountMult>0)
	auc <- performance(pred,"auc")@y.values[[1]]
	print(paste0("F-AUC:", auc))	
	
}


ranking_randomForest <- function (train, test) 
{
	randomForest <- randomForest(BugCountMult ~ LOC + numberOfCommits + CountClassCoupled + MaxInheritanceTree + PercentLackOfCohesion + SumCyclomatic + NumCochangedFiles + coChangedDifferentPackage + coChangedSamePackage + BCO + SPF + BDC + BUO + incomingDep + outgoingDep + internalEdges + externalEdges + edgesInto + edgesOutOf, data= train)
	test.pred <- predict(randomForest, test, type="response")
	spearman <- cor(test$BugCountMult, test.pred, method="spearman")
	spearman.p <- cor.test(test$BugCountMult, test.pred, method="spearman", exact=FALSE)$p.value
	print(paste0("F-spearman: ", spearman, " F-spearman.p: ", spearman.p))
}


classification_glmnb(TrainingData, TestData)
classification_linear(TrainingData, TestData)
classification_randomForest(TrainingData, TestData)



ranking_glmnb(TrainingData, TestData)
ranking_linear(TrainingData, TestData)
ranking_randomForest(TrainingData, TestData)











