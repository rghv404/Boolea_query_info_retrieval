# Boolea_query_info_retrieval
The java code aims to load a solr index and do boolean query retrieval based on term at a time and document at a time methodology
Get postings lists
This method retrieves the postings lists for each of the given query terms. Input of
this method will be a set of terms: term0, term1,..., termN. It should output the
postings for each term in the following format:
GetPostings
term0
Postings list: 1000 2000 3000 … (by increasing document IDs)
GetPostings
term1
Postings list: 1000 3000 6000 … (by increasing document IDs)
…
GetPostings
termN
Postings list: 2000 7000 8000 … (by increasing document IDs)
2. Term-at-a-time AND query
This method is to implement multi-term boolean AND query on the index using
term-at-a-time (TAAT) strategy. Input of this function will be a set of terms: term0,
term1, …, termN. Output format of this method should be:
TaatAnd
term0 term1 … termN
Results: 1000 2000 3000 … (by increasing document IDs)
Number of documents in results: x
Number of comparisons: y
If the result of this query is empty then output:
TaatAnd
term0 term1 … termN
Results: empty
Number of documents in results: 0
Number of comparisons: y
3. Term-at-a-time OR query
This function is to implement multi-term boolean OR query on the index using
TAAT. Input of this function will be a set of query terms: term0, term1, … , termN.
Output format of this method should be:
TaatOr
term0 term1 … termN
Results: 1000 2000 3000 … (by increasing document IDs)
Number of documents in results: x
Number of comparisons: y
NOTE: A comparison (for field “Number of comparisons”) is counted whenever
you compare two Document IDs during the union or intersection operation.
4. Document-at-a-time AND query
This function is to implement multi-term boolean AND query on the index using
document-at-a-time (DAAT) strategy. Input of the function will be a set of query
terms: term0, term1, …, termN. Output the following to the output file:
DaatAnd
term0 term1 … termN
Results: 1000 2000 3000 … (by increasing document IDs)
Number of documents in results: x
Number of comparisons: y
If the result of the query is empty then output:
DaatAnd
term0 term1 … termN
Results: empty
Number of documents in results: 0
Number of comparisons: y
5. Document-at-a-time OR query
This function is to implement multi-term boolean OR query on the index using
DAAT. Input of this function will be a set of query terms: term0, term1, …, termN.
Output the following to the output file:
DaatOr
term0 term1 … termN
Results: 1000 2000 3000 … (by increasing document IDs)
Number of documents in results: x
Number of comparisons: y

Sample Input Output
The input file will be a .txt file containing multiple lines. Each line refers to a set of
query terms which are separated by one blank space. For example:
term0 term1
term2 term3 term4 term5 term6 term7
term8 term9 term10
In this case, the output should be in the following order:
GetPostings term0 term1
TaatAnd term0 term1
TaatOr term0 term1
DaatAnd term0 term1
DaatOr term0 term1
GetPostings term2 term3 term4 term5 term6 term7
TaatAnd term2 term3 term4 term5 term6 term7
TaatOr term2 term3 term4 term5 term6 term7
DaatAnd term2 term3 term4 term5 term6 term7
DaatOr term2 term3 term4 term5 term6 term7
GetPostings term8 term9 term10
TaatAnd term8 term9 term10
TaatOr term8 term9 term10
DaatAnd term8 term9 term10
DaatOr term8 term9 term10
The corresponding output file for the input mentioned before will be:
GetPostings
term0
Postings list: 1000 2000 3000 …
GetPostings
term1
Postings list: 1000 2000 3000 …
TaatAnd
term0 term1
Results: 1000 2000 3000 …
Number of documents in results: x
Number of comparisons: y
TaatOr
term0 term1
Results: 1000 2000 3000 …
Number of documents in results: x
Number of comparisons: y
DaatAnd
term0 term1
Results: 1000 2000 3000 …
Number of documents in results: x
Number of comparisons: y
DaatOr
term0 term1
Results: 1000 2000 3000 …
Number of documents in results: x
Number of comparisons: y
GetPostings
term2
Postings list: 1000 2000 3000 …
GetPostings
term3
Postings list: 1000 2000 3000 …
GetPostings
term4
Postings list: 1000 2000 3000 …
GetPostings
term5
Postings list: 1000 2000 3000 …
GetPostings
term6
Postings list: 1000 2000 3000 …
GetPostings
term7
Postings list: 1000 2000 3000 …
TaatAnd
term2 term3 term4 term5 term6 term7
Results: 1000 2000 3000 …
Number of documents in results: x
Number of comparisons: y
TaatOr
term2 term3 term4 term5 term6 term7
Results: 1000 2000 3000 …
Number of documents in results: x
Number of comparisons: y
DaatAnd
term2 term3 term4 term5 term6 term7
Results: 1000 2000 3000 …
Number of documents in results: x
Number of comparisons: y
DaatOr
term2 term3 term4 term5 term6 term7
Results: 1000 2000 3000 …
Number of documents in results: x
Number of comparisons: y
GetPostings
term8
Postings list: 1000 2000 3000 …
GetPostings
term9
Postings list: 1000 2000 3000 …
GetPostings
term10
Postings list: 1000 2000 3000 …
TaatAnd
term8 term9 term10
Results: 1000 2000 3000 …
Number of documents in results: x
Number of comparisons: y
TaatOr
term8 term9 term10
Results: 1000 2000 3000 …
Number of documents in results: x
Number of comparisons: y
DaatAnd
term8 term9 term10
Results: 1000 2000 3000 …
Number of documents in results: x
Number of comparisons: y
DaatOr
term8 term9 term10
Results: 1000 2000 3000 …
Number of documents in results: x
Number of comparisons: y
