set term png
set output "pr_graph.png"
set title "P/R from trec_eval test Analyzer=English"
set ylabel "Precision"
set xlabel "Recall"
set xrange [0:1]
set yrange [0:1]
set xtics 0,.2,1
set ytics 0,.2,1

plot 'enlish_bm.dat' title "Similarity=BM25" with lines, 'english_s.dat' title "Similarity=Standard" with lines, 'english_c.dat' title "Similarity=Classic" with lines
