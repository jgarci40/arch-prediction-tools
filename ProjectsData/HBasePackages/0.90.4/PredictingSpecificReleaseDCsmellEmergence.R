require(MASS) #glm.nb
require(ROCR)
require(randomForest)



options(digits=2)
script.dir <- dirname(sys.frame(1)$ofile)
TrainingData<-read.csv(file.path(script.dir,"TrainingData.csv"),header=T)
TestData<-read.csv(file.path(script.dir,"TestData.csv"),header=T)

sampleVec <- vector(mode="numeric", length = nrow(TrainingData))
for (i in 1:nrow(TrainingData))
{			
	if (TrainingData[i,"BDC"] == 0 & TrainingData[i,"BDCnextRelease"] == 1)
		sampleVec[i] <- 1

}	
TrainingData$newBDC <- sampleVec
summary(TrainingData$newBDC)
length(which(TrainingData$newBDC == 1))
length(which(TrainingData$newBDC == 1))/nrow(TrainingData)


sampleVec <- vector(mode="numeric", length = nrow(TestData))
for (i in 1:nrow(TestData))
{			
	if (TestData[i,"BDC"] == 0 & TestData[i,"BDCnextRelease"] == 1)
		sampleVec[i] <- 1

}	
TestData$newBDC <- sampleVec
summary(TestData$newBDC)
length(which(TestData$newBDC == 1))
length(which(TestData$newBDC == 1))/nrow(TestData)


summary(m1 <- glm.nb(newBDC ~ log2(LOC+1) + log2(numberOfCommits+1) + log2(CountClassCoupled+1) + log2(MaxInheritanceTree+1) + log2(PercentLackOfCohesion+1) + log2(SumCyclomatic+1) +log2(coChangedDifferentPackage+1)+log2(coChangedSamePackage+1)+ log2(NumCochangedFiles+1) + BDC + BUO +log2(incomingDep+1) + log2(outgoingDep+1) + log2(internalEdges +1) + log2(externalEdges +1) + log2(edgesInto+1) + log2(edgesOutOf+1), data= TrainingData))

step <- stepAIC(m1, direction="both")
step$anova # display results

classification_glmnb <- function (train, test) 
{  
	model.glm.nb <- glm.nb(newBDC ~ log2(SumCyclomatic + 1) + BDC + BUO + log2(incomingDep + 
    1)
, data=train)	
	test.prob <- predict(model.glm.nb, test, type="response")		
	
	pred <- prediction(test.prob, test$newBDC>0)
	auc <- performance(pred,"auc")@y.values[[1]]
	
	#return(list(auc=auc))
	print(paste0(" AUC:", auc))
	
}


summary(m1 <- lm(newBDC ~ LOC + numberOfCommits + CountClassCoupled + MaxInheritanceTree + PercentLackOfCohesion + SumCyclomatic + NumCochangedFiles + coChangedDifferentPackage + coChangedSamePackage + BDC + BUO + incomingDep + outgoingDep + internalEdges + externalEdges + edgesInto + edgesOutOf, data= TrainingData))

step <- stepAIC(m1, direction="both")
step$anova # display results


classification_linear <- function (train, test) 
{
	model.lm <- lm(newBDC ~ CountClassCoupled + BDC + outgoingDep + externalEdges + 
    edgesInto, data=train)
	test.prob <- predict(model.lm, test, type="response")
	pred <- prediction(test.prob, test$newBDC>0)
	auc <- performance(pred,"auc")@y.values[[1]]
	print(paste0(" AUC:", auc))	
}




classification_randomForest <- function (train, test) 
{
	randomForest <- randomForest(newBDC ~ LOC + numberOfCommits + CountClassCoupled + MaxInheritanceTree + PercentLackOfCohesion + SumCyclomatic + NumCochangedFiles + coChangedDifferentPackage + coChangedSamePackage + BDC + BUO + incomingDep + outgoingDep + internalEdges + externalEdges + edgesInto + edgesOutOf, data= train)
	test.prob <- predict(randomForest, test, type="response")
	pred <- prediction(test.prob, test$newBDC>0)
	auc <- performance(pred,"auc")@y.values[[1]]
	print(paste0(" AUC:", auc))	
	
}



classification_glmnb(TrainingData, TestData)
classification_linear(TrainingData, TestData)
classification_randomForest(TrainingData, TestData)



















