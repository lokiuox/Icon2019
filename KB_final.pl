:-dynamic(strada_corrente/2).
:-dynamic(prossima_strada/2).
:-dynamic(partenza/2).
:-dynamic(destinazione/2).
:-dynamic(mitrovo/3).
/* :-dynamic(prima/2). */
:-dynamic(peso/2).
:-dynamic(contamacchine/2).
:-dynamic(velocita/2).
:-dynamic(somma/2).
:-dynamic(conta/2).

/*
numerocivico(K,L):- partenza(K,L).  
numerocivico(K,L):- destinazione(K,L).

numerocivico(_civico,civico1).
*/
partenza(partenza,partenza1).
destinazione(destinazione,destinazione1).
lunghezza(lunghezza,lunghezza1).
angolo(angolo,angolo1).
collega(incrocio1,strada,incrocio2).
coordinata(strada,x,y).
incrocio(incrocio3).
macchina(macchina).
prima(macchina1,macchina2).
velocita(macchina3,velocita).
velocitamedia(strada2,numero).
velocitamax(strada3,numero1).
stop(strada4).
semaforo(strada5).
peso(strada12,assai).
strada_corrente(macchina5,strada7).
prossima_strada(macchina4,strada6).
strada(strada6).
temporosso(strada5,9).


strada_corrente(mac,stra).
strada_corrente(mac1,stra).
strada_corrente(mac2,stra).

lunghezza(stra,10).
velocitamax(stra,100).
semaforo(stra).
stop(stra).
temporosso(stra,10).

velocita(mac,50).
velocita(mac1,60).
velocita(mac2,70).


strada(X):- lunghezza(X,Y).
strada(X):- angolo(X,Z).
strada(D):- collega(C,D,E).
strada(X):- peso(X,Y).
strada(L):- coordinata(L,X,Y).

incrocio(C):- collega(C,D,E).
incrocio(E):- collega(C,D,E).

macchina(K):- strada_corrente(K,L).
strada(L):- strada_corrente(K,L).
/*
macchina(S):- prima(S,D).
macchina(D):- prima(S,D).
*/
macchina(S):- velocita(S,_).

velocitamedia(L,D):- findall(X,strada_corrente(X,L),Y),contamacchine(L,N),somma(Y,C),D is /(C,N).

somma([],0).
somma([X|Y],D):- somma(Y,G),velocita(X,F),D is F + G.

peso(L,X):- 
	lunghezza(L,K),
	contamacchine(L,N), 
	(semaforo(L) -> temporosso(L,J); J is 0),
	(N is 0 -> D is 1,W is 0 ; velocitamedia(L,D), W is 1),
	(stop(L) -> Costante is 3 ; Costante is 0),
	velocitamax(L,F),
	X is +(+(+(/(K,F),/(J,2)),Costante),*(/(K,D),W)).

conta([],0).
conta([H|Coda], N) :- conta(Coda, N1),N is N1 + 1.

contamacchine(L,N):- findall(X,strada_corrente(X,L),Y),conta(Y,N).

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

/*
percorso(A,B,Path,Len) :-
       spostamento(A,B,[A],Q,Len), 
       reverse(Q,Path).

spostamento(A,B,P,[B|P],L) :- 
       connected(A,B,L).
spostamento(A,B,Visited,Path,L) :-
       connected(A,C,D),           
       C \== B,
       \+member(C,Visited),
       travel(C,B,[C|Visited],Path,L1),
       L is D+L1.  

piubreve(A,B,Path,Length) :-
   setof([P,L],path(A,B,P,L),Set),
   Set = [_|_], % fail if empty
   minimal(Set,[Path,Length]).

minimal([F|R],M) :- min(R,F,M).

% minimal path
min([],M,M).
min([[P,L]|R],[_,M],Min) :- L < M, !, min(R,[P,L],Min). 
min([_|R],M,Min) :- min(R,M,Min).

*/