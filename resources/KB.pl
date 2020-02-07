:-dynamic(strada_corrente/2).
:-dynamic(prossima_strada/2).

partenza(k,l).
destinazione(j,v).
numerocivico(K,L):- partenza(K,L).  
numerocivico(K,L):- destinazione(K,L).

strada(z).
strada(d).
strada(X):- lunghezza(X,Y).
strada(X):- angolo(X,Z).
strada(D):- collega(C,D,E).
strada(X):- peso(X,Y).
strada(K):- posizione(K,X,Y).
strada(L):- coordinata(L,X,Y).

incrocio(y).
incrocio(C):- collega(C,D,E).
incrocio(E):- collega(C,D,E).

collega(iz, r0, i0).
collega(ix, r3, i0).
collega(i0, r1, i1).
collega(i1, r2, iy).

lunghezza(x,y).
angolo(bs,180).
angolo(ds,270).
angolo(ps,0).
angolo(ps,360).
angolo(ls,90).
peso(f,n).
angolo(r0,270).
angolo(r1,0).
angolo(r1,360).
angolo(r2,0).
angolo(r2,360).
angolo(r3,90).
rosso(ds).
stop(q).
prima(q,w).

collega(by,bs,y).
collega(dy,ds,y).
collega(py,ps,y).
collega(ly,ls,y).
 
rosso(ds).
stop(q).
prima(q,w).


macchina(K):- mitrovo(K,L,T).
strada(L):- mitrovo(K,L,T).
numerocivico(K,T):- mitrovo(K,L,T).

macchina(S):- prima(S,D).
macchina(D):- prima(S,D).

/*velocitàmedia(L,D):-*/ 

peso(L,X):- =(X,+(+(\(K,F),\(J,2)),costante),\(K,D)),
	lunghezza(L,K),
	temporosso(L,J),
	velocitamedia(L,D),
	velocitamax(L,F).

count([],0).
count([H|Tail], N) :- count(Tail, N1),N is N1 + 1.

contamacchine(L,N):- count(Z,N),findall(X,mitrovo(X,L,T),Z).

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
	(-90 is -(Aa,Ab) ; 90 is -(An,Ab)).

/* precedenza(A): A ha la precendenza */
precedenza(A) :- \+(precedenza(A,B)).

