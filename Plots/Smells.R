library(ggplot2)
library(plyr)
options(digits=2)

change<-read.csv("/Users/joshua/ser_home/projects/arch_prediction/PerRelease/Plots/Smells.csv",header=T)
change$Smell <- factor(change$Smell,
                       levels = c('ARC-CO','ARC-SF','ARC-DC','ARC-LO','Pkg-DC','Pkg-LO'),ordered = TRUE)
p<-ggplot(change, aes(x= Models, y= AUC, fill= Models)) + geom_boxplot(width = 1) + xlab("") +
scale_fill_manual(values = c("L" = "grey100", "N" = "grey50", "F" = "grey75"), labels = c("L" = "LR", "N"="NBR", "F"="RF"))
p+guides(fill=FALSE) 
p+ facet_grid(. ~ Smell) + theme(legend.position="bottom", text = element_text(size=25), panel.background = element_rect(fill='white', colour = 'black'), axis.text.x = element_text(color = "black"), axis.text.y = element_text(color = "black"), strip.text.x = element_text(family="sans", color = "black"))
ggsave(file="/Users/joshua/ser_home/projects/arch_prediction/PerRelease/Plots/Smells.png")


