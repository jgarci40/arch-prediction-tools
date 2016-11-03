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

sampleVec <- vector(mode="numeric", length = nrow(TrainingData))
for (i in 1:nrow(TrainingData))
{			
	if (TrainingData[i,"BUO"] == 0 & TrainingData[i,"BUOnextRelease"] == 1)
		sampleVec[i] <- 1

}	
TrainingData$newBUO <- sampleVec
summary(TrainingData$newBUO)
length(which(TrainingData$newBUO == 1))
length(which(TrainingData$newBUO == 1))/nrow(TrainingData)


sampleVec <- vector(mode="numeric", length = nrow(TestData))
for (i in 1:nrow(TestData))
{			
	if (TestData[i,"BUO"] == 0 & TestData[i,"BUOnextRelease"] == 1)
		sampleVec[i] <- 1

}	
TestData$newBUO <- sampleVec
summary(TestData$newBUO)
length(which(TestData$newBUO == 1))
length(which(TestData$newBUO == 1))/nrow(TestData)


summary(m1 <- glm.nb(newBUO ~ log2(LOC+1) + log2(numberOfCommits+1) + log2(CountClassCoupled+1) + log2(MaxInheritanceTree+1) + log2(PercentLackOfCohesion+1) + log2(SumCyclomatic+1) +log2(coChangedDifferentPackage+1)+log2(coChangedSamePackage+1)+ log2(NumCochangedFiles+1) + BCO + SPF + BDC + BUO +log2(incomingDep+1) + log2(outgoingDep+1) + log2(internalEdges +1) + log2(externalEdges +1) + log2(edgesInto+1) + log2(edgesOutOf+1), data= TrainingData))

step <- stepAIC(m1, direction="both")
step$anova # display results

classification_glmnb <- function (train, test) 
{  
	model.glm.nb <- glm.nb(newBUO ~ log2(LOC + 1) + log2(CountClassCoupled + 1) + log2(PercentLackOfCohesion + 
    1) + BCO + BUO + log2(externalEdges + 1)
, data=train)	
	test.prob <- predict(model.glm.nb, test, type="response")		
	
	pred <- prediction(test.prob, test$newBUO>0)
	auc <- performance(pred,"auc")@y.values[[1]]
	
	#return(list(auc=auc))
	print(paste0("N-AUC:", auc))
	
}



summary(m1 <- lm(newBUO ~ LOC + numberOfCommits + CountClassCoupled + MaxInheritanceTree + PercentLackOfCohesion + SumCyclomatic + NumCochangedFiles + coChangedDifferentPackage + coChangedSamePackage + BCO + SPF + BDC + BUO + incomingDep + outgoingDep + internalEdges + externalEdges + edgesInto + edgesOutOf, data= TrainingData))

step <- stepAIC(m1, direction="both")
step$anova # display results


classification_linear <- function (train, test) 
{
	model.lm <- lm(newBUO ~ MaxInheritanceTree + coChangedSamePackage + BCO + SPF + 
    BUO + incomingDep + outgoingDep + externalEdges, data=train)
	test.prob <- predict(model.lm, test, type="response")
	pred <- prediction(test.prob, test$newBUO>0)
	auc <- performance(pred,"auc")@y.values[[1]]
	print(paste0("L-AUC:", auc))	
}




classification_randomForest <- function (train, test) 
{
	randomForest <- randomForest(newBUO ~ LOC + numberOfCommits + CountClassCoupled + MaxInheritanceTree + PercentLackOfCohesion + SumCyclomatic + NumCochangedFiles + coChangedDifferentPackage + coChangedSamePackage + BCO + SPF + BDC + BUO + incomingDep + outgoingDep + internalEdges + externalEdges + edgesInto + edgesOutOf, data= train)
	test.prob <- predict(randomForest, test, type="response")
	pred <- prediction(test.prob, test$newBUO>0)
	auc <- performance(pred,"auc")@y.values[[1]]
	print(paste0("F-AUC:", auc))	
	
}

classification_glmnb(TrainingData, TestData)
classification_linear(TrainingData, TestData)
classification_randomForest(TrainingData, TestData)








