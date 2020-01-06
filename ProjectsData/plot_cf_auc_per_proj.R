library(ggplot2)
library(plyr)
options(digits = 2)

files <-
  list.files(
    path = "/Users/joshua/ser_home/projects/arch_prediction/PerRelease/ProjectsData/",
    pattern = "*_cf_auc\\.csv",
    full.names = T,
    recursive = FALSE
  )
lapply(files, function(csv_file) {
  change <- read.csv(csv_file, header = T)
  p <-
    ggplot() + geom_boxplot(data = change,
                            aes(x = Models, y = AUC, fill = Models),
                            width = 0.6) + xlab("") +
    scale_fill_manual(
      values = c("L" = "grey100", "N" = "grey50", "F" = "grey75"),
      labels = c("L" = "LR", "N" = "NBR", "F" = "RF")
    )
  p + guides(fill = FALSE)
  p + facet_grid(. ~ Recovery) + theme(
    legend.position = "bottom",
    text = element_text(size = 30),
    panel.background = element_rect(fill = 'white', colour = 'black'),
    axis.text.x = element_text(color = "black"),
    axis.text.y = element_text(color = "black"),
    strip.text.x = element_text(family = "sans", color = "black")
  )
  ggsave(file = paste(sub("^([^.]*).*", "\\1", csv_file), ".png", sep = ''))
})

