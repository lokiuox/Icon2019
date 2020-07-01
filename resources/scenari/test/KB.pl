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

semaforo(r6).
stop(r6).
temporosso(r6,1).

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


velocitamedia(L,D):- findall(X,strada_corrente(X,L),Y),contamacchine(L,N),somma(Y,C),D is /(C,N).

somma([],0).
somma([X|Y],D):- somma(Y,G),velocita(X,F,_L),D is F + G.


peso(L,X):-
	lunghezza(L,K),
	contamacchine(L,N),
	(semaforo(L) -> J is 2.5*30; J is 0),
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
	(1 is -(Aa,An) -> fail; ( (0 is -(Aa,An), -1 is -(An,Ab)) -> true; (-1 is -(Aa,An),(0 is -(An,Ab) ; -1 is -(An,Ab))) -> true ; ((-2 is -(Aa,An),(1 is -(An,Ab) ; 0 is -(An,Ab) ; -1 is -(An,Ab))) -> true ; fail))).

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

strada(r6).
strada(r8).
strada(r90).
strada(r141).
strada(r93).
strada(r96).
strada(r51).
strada(r140).
strada(r54).
strada(r13).
strada(r135).
strada(r15).
strada(r137).
strada(r132).
strada(r133).
strada(r61).
strada(r65).
strada(r20).
strada(r128).
strada(r27).
strada(r26).
strada(r29).
strada(r72).
strada(r71).
strada(r74).
strada(r32).
strada(r75).
strada(r78).
strada(r37).
strada(r39).
strada(r85).
strada(r40).
strada(r43).
strada(r86).
strada(r47).
strada(r46).
strada(r60).
strada(r24).
strada(r138).
strada(r69).
strada(r82).
strada(r64).
strada(r53).
strada(r81).
strada(r92).
strada(r95).
strada(r57).
strada(r136).
strada(r33).

angolo(r6,3).
angolo(r8,0).
angolo(r90,2).
angolo(r141,2).
angolo(r93,2).
angolo(r96,2).
angolo(r51,1).
angolo(r140,1).
angolo(r54,1).
angolo(r13,0).
angolo(r135,1).
angolo(r15,1).
angolo(r137,0).
angolo(r132,2).
angolo(r133,0).
angolo(r61,0).
angolo(r65,0).
angolo(r20,1).
angolo(r128,0).
angolo(r27,2).
angolo(r26,3).
angolo(r29,3).
angolo(r72,2).
angolo(r71,3).
angolo(r74,3).
angolo(r32,0).
angolo(r75,2).
angolo(r78,0).
angolo(r37,1).
angolo(r39,2).
angolo(r85,1).
angolo(r40,1).
angolo(r43,3).
angolo(r86,0).
angolo(r47,3).
angolo(r46,0).
angolo(r60,1).
angolo(r24,2).
angolo(r138,1).
angolo(r69,2).
angolo(r82,0).
angolo(r64,1).
angolo(r53,2).
angolo(r81,1).
angolo(r92,3).
angolo(r95,3).
angolo(r57,0).
angolo(r136,2).
angolo(r33,3).

velocitamax(r6,8).
velocitamax(r8,8).
velocitamax(r90,8).
velocitamax(r141,8).
velocitamax(r93,8).
velocitamax(r96,8).
velocitamax(r51,8).
velocitamax(r140,8).
velocitamax(r54,8).
velocitamax(r13,8).
velocitamax(r135,8).
velocitamax(r15,8).
velocitamax(r137,8).
velocitamax(r132,8).
velocitamax(r133,8).
velocitamax(r61,8).
velocitamax(r65,8).
velocitamax(r20,8).
velocitamax(r128,8).
velocitamax(r27,8).
velocitamax(r26,8).
velocitamax(r29,8).
velocitamax(r72,8).
velocitamax(r71,8).
velocitamax(r74,8).
velocitamax(r32,8).
velocitamax(r75,8).
velocitamax(r78,8).
velocitamax(r37,8).
velocitamax(r39,8).
velocitamax(r85,8).
velocitamax(r40,8).
velocitamax(r43,8).
velocitamax(r86,8).
velocitamax(r47,8).
velocitamax(r46,8).
velocitamax(r60,8).
velocitamax(r24,8).
velocitamax(r138,8).
velocitamax(r69,8).
velocitamax(r82,8).
velocitamax(r64,8).
velocitamax(r53,8).
velocitamax(r81,8).
velocitamax(r92,8).
velocitamax(r95,8).
velocitamax(r57,8).
velocitamax(r136,8).
velocitamax(r33,8).

lunghezza(r6,160).
lunghezza(r8,224).
lunghezza(r90,224).
lunghezza(r141,480).
lunghezza(r93,224).
lunghezza(r96,224).
lunghezza(r51,160).
lunghezza(r140,224).
lunghezza(r54,160).
lunghezza(r13,512).
lunghezza(r135,224).
lunghezza(r15,192).
lunghezza(r137,480).
lunghezza(r132,480).
lunghezza(r133,480).
lunghezza(r61,224).
lunghezza(r65,224).
lunghezza(r20,192).
lunghezza(r128,480).
lunghezza(r27,224).
lunghezza(r26,160).
lunghezza(r29,192).
lunghezza(r72,224).
lunghezza(r71,224).
lunghezza(r74,192).
lunghezza(r32,224).
lunghezza(r75,512).
lunghezza(r78,224).
lunghezza(r37,224).
lunghezza(r39,224).
lunghezza(r85,160).
lunghezza(r40,224).
lunghezza(r43,224).
lunghezza(r86,512).
lunghezza(r47,224).
lunghezza(r46,224).
lunghezza(r60,192).
lunghezza(r24,512).
lunghezza(r138,192).
lunghezza(r69,224).
lunghezza(r82,224).
lunghezza(r64,224).
lunghezza(r53,224).
lunghezza(r81,224).
lunghezza(r92,160).
lunghezza(r95,224).
lunghezza(r57,224).
lunghezza(r136,480).
lunghezza(r33,192).

coordinata(r6,48,96).
coordinata(r8,96,80).
coordinata(r90,320,48).
coordinata(r141,1440,560).
coordinata(r93,320,272).
coordinata(r96,320,560).
coordinata(r51,944,256).
coordinata(r140,1456,544).
coordinata(r54,80,256).
coordinata(r13,96,848).
coordinata(r135,1456,800).
coordinata(r15,80,800).
coordinata(r137,960,304).
coordinata(r132,1440,48).
coordinata(r133,960,80).
coordinata(r61,672,592).
coordinata(r65,672,304).
coordinata(r20,944,800).
coordinata(r128,960,848).
coordinata(r27,896,816).
coordinata(r26,912,96).
coordinata(r29,48,608).
coordinata(r72,608,560).
coordinata(r71,624,320).
coordinata(r74,624,608).
coordinata(r32,96,592).
coordinata(r75,608,816).
coordinata(r78,384,592).
coordinata(r37,944,544).
coordinata(r39,896,560).
coordinata(r85,368,256).
coordinata(r40,80,544).
coordinata(r43,48,320).
coordinata(r86,384,80).
coordinata(r47,912,320).
coordinata(r46,96,304).
coordinata(r60,656,800).
coordinata(r24,896,48).
coordinata(r138,1456,288).
coordinata(r69,608,272).
coordinata(r82,384,304).
coordinata(r64,656,544).
coordinata(r53,896,272).
coordinata(r81,368,544).
coordinata(r92,336,96).
coordinata(r95,336,320).
coordinata(r57,672,848).
coordinata(r136,1440,816).
coordinata(r33,912,608).

stop(r141).
stop(r81).
stop(r92).
stop(r95).

semaforo(r82).
semaforo(r64).
semaforo(r53).
semaforo(r57).
semaforo(r136).
semaforo(r33).

incrocio(i20).
incrocio(i1).
incrocio(i11).
incrocio(i22).
incrocio(i2).
incrocio(i10).
incrocio(i21).
incrocio(i3).
incrocio(i13).
incrocio(i4).
incrocio(i12).
incrocio(i5).
incrocio(i6).
incrocio(i14).
incrocio(i7).
incrocio(i8).
incrocio(i9).
incrocio(i19).

collega(i20,r132,i4).
collega(i1,r6,i7).
collega(i1,r8,i14).
collega(i11,r69,i13).
collega(i11,r71,i10).
collega(i11,r65,i8).
collega(i22,r141,i6).
collega(i22,r140,i21).
collega(i2,r15,i5).
collega(i2,r13,i9).
collega(i10,r72,i12).
collega(i10,r64,i11).
collega(i10,r74,i9).
collega(i10,r61,i6).
collega(i21,r138,i20).
collega(i3,r27,i9).
collega(i3,r20,i6).
collega(i3,r128,i19).
collega(i13,r93,i7).
collega(i13,r85,i14).
collega(i13,r95,i12).
collega(i13,r82,i11).
collega(i4,r24,i14).
collega(i4,r26,i8).
collega(i4,r133,i20).
collega(i12,r96,i5).
collega(i12,r81,i13).
collega(i12,r78,i10).
collega(i5,r40,i7).
collega(i5,r29,i2).
collega(i5,r32,i12).
collega(i6,r39,i10).
collega(i6,r37,i8).
collega(i6,r33,i3).
collega(i14,r90,i1).
collega(i14,r92,i13).
collega(i14,r86,i4).
collega(i7,r54,i1).
collega(i7,r43,i5).
collega(i7,r46,i13).
collega(i8,r53,i11).
collega(i8,r51,i4).
collega(i8,r47,i6).
collega(i8,r137,i21).
collega(i9,r75,i2).
collega(i9,r60,i10).
collega(i9,r57,i3).
collega(i19,r136,i3).
collega(i19,r135,i22).
