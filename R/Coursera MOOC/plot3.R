data <- read.table("household_power_consumption.txt", sep=";", header=TRUE)
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
plot(data[66637:69516,"Date"], sub1, type="l",ylab="Energy sub metering",xlab="")
lines(data[66637:69516,"Date"], sub2, type="l",col="red")
lines(data[66637:69516,"Date"], sub3, type="l",col="blue")
axis(side=1, at=seq(0, 35, by=10))
legend("topright", names(data[,7:9]), cex=0.8, col=plot_colors, lwd=2)
dev.copy(png, file = "plot3.png",width = 480, height = 480, units = 'px')
dev.off()

##sub1<-data[66637:69516,7]
##sub2<-data[66637:69516,8]
##sub3<-data[66637:69516,9]
##cha10<-which(sub1>10&sub1<15)
##sub1[cha10]<-sub1[cha10]/5
##cha8<-which(sub1>8&sub1<=10)
##sub1[cha8]<-sub1[cha8]/4
##cha6<-which(sub1>6&sub1<=8)
##sub1[cha8]<-sub1[cha8]/3
##cha4<-which(sub1>4&sub1<=6)
##sub1[cha8]<-sub1[cha8]/2
