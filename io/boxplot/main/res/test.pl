incrocio(A)
strada(B)

partenza(K,L):-
  strada(K)
  
destinazione(K,L):-
  strada(K)

collega(C,D,E):-
	 incrocio(C),
	 strada(D),
	 incrocio(E)

peso(X,Y):-
	 strada(X)

lunghezza(X,Y):-
	strada(X)

angolo(X,Z):-
	 strada(X)

