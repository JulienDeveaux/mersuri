set terminal png size 800, 600
set title "Average of virus propagation"
set xlabel 'time'
set ylabel 'infectés'
set output 'Scenario.png'
set key outside

plot 'Scenario.dat' index 0 using 1:2 smooth csplines title 'Average infected scenario 1', \
	'Scenario.dat' index 1 using 1:2 smooth csplines title 'Average infected scenario 2'