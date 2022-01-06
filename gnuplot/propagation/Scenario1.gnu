set terminal png size 800, 600
set title "Average of virus propagation / scenario 1"
set xlabel 'time'
set ylabel 'infectés'
set output 'Scenario1.png'
set key outside

plot 'Scenario1.dat' using 1:2 smooth csplines title 'Average infected'