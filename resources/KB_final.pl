:-dynamic(strada_corrente/2).
:-dynamic(prossima_strada/2).
:-dynamic(partenza/2).
:-dynamic(destinazione/2).
:-dynamic(mitrovo/3).
/*
:-dynamic(prima/2).
:-dynamic(numerocivico/2).
 */
:-dynamic(rosso/1).
:-dynamic(peso/2).
:-dynamic(contamacchine/2).
:-dynamic(velocita/3).
:-dynamic(somma/2).
:-dynamic(conta/2).

/*
numerocivico(K,L):- partenza(K,L).
numerocivico(K,L):- destinazione(K,L).

numerocivico(_civico,civico1).
*/
partenza(partenza,partenza1).
destinazione(destinazione,destinazione1).
angolo(angolo,angolo1).
collega(incrocio1,strada,incrocio2).
coordinata(strada33,x,y).
/* prima(macchina1,macchina2). */
prossima_strada(macchina4,strada6).
strada_corrente(macchina5,strada7).
lunghezza(lunghezza1,lunghezza2).
velocitamax(strada3,numero1).
semaforo(strada5).
stop(strada4).
temporosso(strada5,9).
velocita(macchina3,velocitas,stradas).
strada(strada6).

strada(X):- lunghezza(X,_Y).
strada(X):- angolo(X,_Z).
strada(D):- collega(_C,D,_E).
strada(X):- peso(X,_Y).
strada(L):- coordinata(L,_X,_Y).
strada(L):- strada_corrente(_K,L).
strada(L):- velocita(_S,_E,L).

incrocio(incrocio3).

incrocio(C):- collega(C,_D,_E).
incrocio(E):- collega(_C,_D,E).

macchina(macchina).

macchina(K):- strada_corrente(K,_L).
macchina(S):- velocita(S,_E,_L).

/*
macchina(S):- prima(S,D).
macchina(D):- prima(S,D).
*/

velocitamedia(strada3,qualcosa).

velocitamedia(L,D):- findall(X,strada_corrente(X,L),Y),contamacchine(L,N),somma(Y,C),D is /(C,N).

somma([],0).
somma([X|Y],D):- somma(Y,G),velocita(X,F,_L),D is F + G.

peso(strada12,assai).

peso(L,X):-
	lunghezza(L,K),
	contamacchine(L,N),
	(semaforo(L) -> temporosso(L,J); J is 0),
	(N is 0 -> D is 1,W is 0 ; velocitamedia(L,D), W is 1),
	(stop(L) -> Costante is 3 ; Costante is 0),
	velocitamax(L,F),
	X is +(+(+(/(K,F),/(J,2)),Costante),*(/(K,D),W)).

conta([],0).
conta([_H|Coda], N) :- conta(Coda, N1),N is N1 + 1.

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
precedenza(A) :- \+(precedenza(A,_B)),strada_corrente(A,L),\+(rosso(L)).

percorso(A,B,Path,Len) :-
       spostamento(A,B,[A],Q,Len),
       reverse(Q,Path).

spostamento(A,B,P,[B|P],L) :-
       collega(A,C,B),
	   peso(C,L).
spostamento(A,B,Visitato,Path,L) :-
		collega(A,D,C),
		peso(D,F),
		C \== B,
		\+member(C,Visitato),
		spostamento(C,B,[C|Visitato],Path,L1),
		L is F+L1.

piubreve(A,B,Path,Length) :-
   setof([P,L],percorso(A,B,P,L),Set),
   Set = [_|_],
   minimo(Set,[Path,Length]).

minimo([F|R],M) :- min(R,F,M).

min([],M,M).
min([[P,L]|R],[_,M],Min) :- L < M, !, min(R,[P,L],Min).
min([_|R],M,Min) :- min(R,M,Min).