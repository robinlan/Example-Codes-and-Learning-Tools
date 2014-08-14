function squaredError = errorMeasure1(theta, data)
if nargin<1; return; end
x = data(:,1);
y = data(:,2);
y2 = theta(1)*exp(theta(3)*x)+theta(2)*exp(theta(4)*x);
squaredError = sum((y-y2).^2);