set terminal png size 800, 600
set title "Average of virus propagation on a random graph"
set xlabel 'time'
set ylabel 'infectés'
set output 'aleaScenarios.png'
set key outside

plot 'aleaScenarios.dat' index 0 using 1:2 smooth csplines title 'Average infected scenario 1' lt rgb "blue", \
	'aleaScenarios.dat' index 1 using 1:2 smooth csplines title 'Average infected scenario 2' lt rgb "red", \
	'aleaScenarios.dat' index 2 using 1:2 smooth csplines title 'Average infected scenario 3' lt rgb "green"
