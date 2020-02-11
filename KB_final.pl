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
partenza(_partenza,_partenza1).
destinazione(_destinazione,_destinazione1).
lunghezza(_lunghezza,_lunghezza1).
angolo(_angolo,_angolo1).
collega(_incrocio1,_strada,_incrocio2).
coordinata(_strada,_x,_y).
incrocio(_incrocio3).
macchina(_macchina).
prima(_macchina1,_macchina2).
velocita(_macchina3,_velocita).
velocitamedia(_strada2,_numero).
velocitamax(_strada3,_numero1).
stop(_strada4).
semaforo(_strada5).
prossima_strada(_macchina4,_strada6).
strada_corrente(_macchina5,_strada7).
strada(_strada6).


strada_corrente(mac,stra).
strada_corrente(mac1,stra).
strada_corrente(mac2,stra).

velocita(mac,50).
velocita(ma1,60).
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
macchina(S):- velocita(S,F).
/*
velocitamedia(L,D):- =(D,/(somma(Z,T),N)),findall(X,mitrovo(X,L,T),Z),contamacchine(L,N).

somma([],0).
somma([X|Y],D):- =(D,+(F,G)),velocita(X,F),somma([Y],G).



peso(L,X):- =(X,+(+(+(/(K,F),/(J,2)),Costante),/(K,D))),
	lunghezza(L,K),
	(semaforo(L) -> temporosso(L,J); 0 is J),
	(contamacchine(L,N), \+(=(N,0)) -> velocitamedia(L,D);0 is D),
	(stop(L) -> 3 is Costante;0 is Costante),
	velocitamedia(L,D),
	velocitamax(L,F).

conta([],0).
conta([H|Coda], N) :- conta(Coda, N1),N is N1 + 1.

contamacchine(L,N):- conta(Z,N),findall(X,mitrovo(X,L,T),Z).
*/
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