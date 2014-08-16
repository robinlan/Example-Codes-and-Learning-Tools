data <- read.table("household_power_consumption.txt", sep=";", header=TRUE)
data[,3]<-as.numeric(data[, 3])
x<-data[66637:69516,3]
hist(x/500,col="red",xlab="Global Active Power(killowatts)",main="")
title(main="Global Active Power")
dev.copy(png, file = "plot1.png",width = 480, height = 480, units = 'px')
dev.off()