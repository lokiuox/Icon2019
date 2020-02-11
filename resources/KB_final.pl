:-dynamic(strada_corrente/2).
:-dynamic(prossima_strada/2).
:-dynamic(partenza/2).
:-dynamic(destinazione/2).
:-dynamic(mitrovo/3).
:-dynamic(prima/2).
:-dynamic(peso/2).
:-dynamic(contamacchine/2).
:-dynamic(velocita/2).
:-dynamic(somma/2).
:-dynamic(conta/2).

numerocivico(K,L):- partenza(K,L).
numerocivico(K,L):- destinazione(K,L).

strada(X):- lunghezza(X,Y).
strada(X):- angolo(X,Z).
strada(D):- collega(C,D,E).
strada(X):- peso(X,Y).
strada(K):- posizione(K,X,Y).
strada(L):- coordinata(L,X,Y).

incrocio(C):- collega(C,D,E).
incrocio(E):- collega(C,D,E).

macchina(K):- mitrovo(K,L,T).
strada(L):- mitrovo(K,L,T).
numerocivico(K,T):- mitrovo(K,L,T).

macchina(S):- prima(S,D).
macchina(D):- prima(S,D).
macchina(S):- velocita(S,F).

velocitàmedia(L,D):- =(D,/(somma(Z,T),N)),findall(X,mitrovo(X,L,T),Z),contamacchine(L,N).

somma([],0).
somma([X|Y],D):- =(D,+(F,G)),velocita(X,F),somma([Y],G).

peso(L,X):- =(X,+(+(+(/(K,F),/(J,2)),Costante),/(K,D)),
	lunghezza(L,K),
	(semaforo(L) -> temporosso(L,J); 0 is J),
	(contamacchine(L,N), \+(=(N,0)) -> velocitamedia(L,D);0 is D),
	(stop(L) -> 3 is Costante;0 is Costante),
	velocitamedia(L,D),
	velocitamax(L,F).

conta([],0).
conta([H|Coda], N) :- conta(Coda, N1),N is N1 + 1.

contamacchine(L,N):- conta(Z,N),findall(X,mitrovo(X,L,T),Z).

/* precedenza(A,B): A deve dare la precedenza a B */
precedenza(A,B) :-
	prossima_strada(A,Rn),
	strada_corrente(A,Ra),
	strada_corrente(B,Rb),
	((stop(Ra), \+(stop(Rb))-> true;fail) ; (rosso(Ra), \+(rosso(Rb))-> true;fail) ; (\+(stop(Ra)),\+(rosso(Ra))->true;fail)),
	\+(A=B),
	(rosso(Rb)-> fail;true),
	(stop(Rb)-> fail;true),
	angolo(Rn, An),
	angolo(Ra, Aa),
	angolo(Rb, Ab),
	(-1 is -(Aa,Ab) ; 1 is -(An,Ab)).

/* precedenza(A): A ha la precendenza */
precedenza(A) :- \+(precedenza(A,B)).
