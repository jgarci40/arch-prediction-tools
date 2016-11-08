change<-read.csv("/Users/Ehsan/Workspace/RData/Scripts/PerRelease/Plots/multipleReleases/numberOfModules.csv",header=T)
p<-ggplot(change, aes(x= Projects, y= Modules, fill= Projects)) + geom_boxplot(width = 0.6) + xlab("") +
scale_fill_manual(values = c("Cam" = "grey100", "Cas" = "grey90", "Hb" = "grey80", "Hi" = "grey70", "Op" = "grey60"))
p+guides(fill=FALSE) 
p+ facet_grid(. ~ Recovery) + theme(legend.position="bottom", text = element_text(size=23), panel.background = element_rect(fill='white', colour = 'black'), axis.text.x = element_text(color = "black"), axis.text.y = element_text(color = "black"), strip.text.x = element_text(family="sans", color = "black"))
ggsave(file="/Users/Ehsan/Workspace/RData/Scripts/PerRelease/Plots/multipleReleases/numberOfModules.png")