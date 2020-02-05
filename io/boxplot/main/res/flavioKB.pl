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

incrocio(y).
incrocio(C):- collega(C,D,E).
incrocio(E):- collega(C,D,E).
collega(c,d,e).

lunghezza(x,y).
angolo(q,w).
peso(f,n).

macchina(K):- mitrovo(K,L,T).
strada(L):- mitrovo(K,L,T).
numerocivico(K,T):- mitrovo(K,L,T).

macchina(S):- prima(S,D).
macchina(D):- prima(S,D).