# Assignment 1: Information Retrieval & Web Search (CS7IS3)

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

My Azure has the files

  - The full source code directory named as **ss/LuceneFinal**.
  - The **trec_eval** folder is also in the **src** folder of project folder
  - The **CranField** whihc constains **Corrected Qrels** named as  **qrelnew** is in **src** folder of project folder



## Getting Started

- Run the Putty Software with server id 52.168.166.169 and a .ppk file 

### Follow below steps steps:

```sh
$ azureuser
$ sudo su -

``` 
### Building the code

```sh
$ cd LuceneFinal/
$ mvn package
```

### Running the code

```sh
$ java -jar target/LuceneFinal-0.0.1-SNAPSHOT.jar
```
### Result Evaluation

```sh
$ cd src/trec_eval-9.0.7/
$ make
$ ./trec_eval ../cran/qrelnew ../EnglishAnalyzer_BM25Similarity.out
```
**To Evaluate more with different Analyser and Similarity run the following:**

```sh
$ ./trec_eval ../cran/qrelnew ../EnglishAnalyzer_LMDirichletSimilarity.out
$ ./trec_eval ../cran/qrelnew ../EnglishAnalyzer_ClassicSimilarity.out
$ ./trec_eval ../cran/qrelnew ../StandardAnalyzer_ClassicSimilarity.out
$ ./trec_eval ../cran/qrelnew ../StandardAnalyzer_BM25Similarity.out
$ ./trec_eval ../cran/qrelnew ../StandardAnalyzer_LMDirichletSimilarity.out
```
