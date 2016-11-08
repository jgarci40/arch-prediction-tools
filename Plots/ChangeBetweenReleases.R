change<-read.csv("/Users/joshua/ser/projects/arch_prediction/PerRelease/Plots/ChangeBetweenReleases.csv",header=T)
p<-ggplot(change, aes(x= Smells, y= Change, fill= Smells)) + geom_boxplot(width = 0.8) + xlab("") + ylab("Change(%)") +
scale_fill_manual(values = c("SF" = "grey100", "CO" = "grey90", "DC" = "grey80", "LO" = "grey70"))
p+guides(fill=FALSE) 
p+ facet_grid(. ~ Recovery) + theme(legend.position="bottom", text = element_text(size=30), panel.background = element_rect(fill='white', colour = 'black'), axis.text.x = element_text(color = "black"), axis.text.y = element_text(color = "black"), strip.text.x = element_text(family="sans", color = "black"))
ggsave(file="/Users/joshua/ser/projects/arch_prediction/PerRelease/Plots/ChangeBetweenReleases.png")