set terminal png
set title "Degree distribution"
set xlabel 'average degree'
set ylabel 'number of nodes'
set output 'dd_dblp_distrib_distance.png'
set style fill solid
set boxwidth 0.5
set yrange [0:250000]
set style line 1 lt rgb "green" lw 2

plot "dd_dblp_dist.dat" smooth csplines title 'degree curve extrapolation', \
 "dd_dblp_dist.dat" with points title 'degree dots', \
 "dd_dblp_dist.dat" using 1:2:xtic(1) with boxes title 'degree bar'