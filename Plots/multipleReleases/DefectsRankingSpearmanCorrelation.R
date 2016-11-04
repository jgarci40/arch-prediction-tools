library(ggplot2)
library(plyr)
options(digits=2)


change<-read.csv("/Users/joshua/ser/projects/arch_prediction/PerRelease/Plots/multipleReleases/DefectsRankingSpearmanCorrelation.csv",header=T)
p<-ggplot(change, aes(x= Models, y= Spearman, fill= Models)) + geom_boxplot(width = 0.6) + xlab("") +
scale_fill_manual(values = c("L" = "grey100", "N" = "grey50", "F" = "grey75"), labels = c("L" = "LR", "N"="NBR", "F"="RF"))
p+guides(fill=FALSE) 
p+ facet_grid(. ~ Recovery) + theme(legend.position="bottom", text = element_text(size=30), panel.background = element_rect(fill='white', colour = 'black'), axis.text.x = element_text(color = "black"), axis.text.y = element_text(color = "black"), strip.text.x = element_text(family="sans", color = "black"))
ggsave(file="/Users/joshua/ser/projects/arch_prediction/PerRelease/Plots/multipleReleases/DefectsRankingSpearmanCorrelation.png")


