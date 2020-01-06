library(ggplot2)
library(plyr)
options(digits = 2)

files <-
  list.files(
    path = "/Users/joshua/ser_home/projects/arch_prediction/PerRelease/ProjectsData/",
    pattern = "*_smells_em\\.csv",
    full.names = T,
    recursive = FALSE
  )
lapply(files, function(csv_file) {
  if (file.info(csv_file)$size == 0) {
    return(NULL)
  }
  
  change <- read.csv(csv_file, header = T)
  change$Smell <- factor(
    change$Smell,
    levels = c('ARC-CO_EM', 'ARC-SF_EM', 'ARC-DC_EM', 'ARC-LO_EM', 'Pkg-DC_EM', 'Pkg-LO_EM'),
    ordered = TRUE
  )
  p <-
    ggplot(change, aes(x = Models, y = AUC, fill = Models)) + geom_boxplot(width = 1) + xlab("") +
    scale_fill_manual(
      values = c("L" = "grey100", "N" = "grey50", "F" = "grey75"),
      labels = c("L" = "LR", "N" = "NBR", "F" = "RF")
    )
  p + guides(fill = FALSE)
  p + facet_grid(. ~ Smell) + theme(
    legend.position = "bottom",
    text = element_text(size = 25),
    panel.background = element_rect(fill = 'white', colour = 'black'),
    axis.text.x = element_text(color = "black"),
    axis.text.y = element_text(color = "black"),
    strip.text.x = element_text(family = "sans", color = "black")
  )
  ggsave(file = paste(sub("^([^.]*).*", "\\1", csv_file), ".png", sep = ''))
})
