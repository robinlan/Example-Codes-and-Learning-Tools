data <- read.table("household_power_consumption.txt", sep=";", header=TRUE)
data[,3]<-as.numeric(data[, 3])
data[,4]<-as.numeric(data[, 4])
data[,5]<-as.numeric(data[, 5])
data[,7]<-as.numeric(data[, 7])
data[,8]<-as.numeric(data[, 8])
data[,9]<-as.numeric(data[, 9])
data$Date <- strptime(paste(data$Date,data$Time), "%d/%m/%Y %H:%M:%S")
Sys.setlocale("LC_TIME", "English")
sub1<-data[66637:69516,7]
sub2<-data[66637:69516,8]
sub3<-data[66637:69516,9]
plot_colors<-NULL
plot_colors[1]<-"black"
plot_colors[2]<-"red"
plot_colors[3]<-"blue"
par(mfrow = c(2, 2))
with(data, {
plot(data[66637:69516,"Date"], data[66637:69516,3]/500, type="l",ylab="Global Active Power",xlab="")
plot(data[66637:69516,"Date"], data[66637:69516,5]/8.5, type="l",ylab="Voltage",xlab="")
plot(data[66637:69516,"Date"], sub1, type="l",ylab="Energy sub metering",xlab="")
lines(data[66637:69516,"Date"], sub2, type="l",col="red")
lines(data[66637:69516,"Date"], sub3, type="l",col="blue")
legend("topright", names(data[,7:9]), cex=0.8, col=plot_colors, lwd=2)
axis(side=1, at=seq(0, 35, by=10))
plot(data[66637:69516,"Date"], data[66637:69516,4]/500, type="l",ylab="Global_reactive_power",xlab="datetime")
})
dev.copy(png, file = "plot4.png",width = 480, height = 480, units = 'px')
dev.off()