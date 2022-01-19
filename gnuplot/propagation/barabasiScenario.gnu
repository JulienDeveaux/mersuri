set terminal png size 800, 600
set title "Average of virus propagation on a barabasiAlbert generated graph"
set xlabel 'time'
set ylabel 'infectés'
set output 'barabasiScenario.png'
set key outside
set yrange [0:300000]

plot 'barabasiScenarios.dat' index 0 using 1:2 smooth csplines title 'Average infected scenario 1' lt rgb "blue", \
	'barabasiScenarios.dat' index 1 using 1:2 smooth csplines title 'Average infected scenario 2' lt rgb "red", \
	'barabasiScenarios.dat' index 2 using 1:2 smooth csplines title 'Average infected scenario 3' lt rgb "green"
