# Progetto Ingegneria Della Conoscenza - Patterson Group AA 2019/20
Il progetto prevede un sistema di gestione del traffico basato sulla condivisione delle informazioni a corta distanza tra gli utenti, cioè i veicoli presenti sulla strada, in modo da ottimizzare il calcolo del percorso ottimale verso la destinazione prestabilita per ciascun veicolo.    
Il sistema può essere esteso anche ad elementi di controllo del traffico, come ad esempio i semafori, che potrebbero adattare il loro tempo di accensione del verde/rosso in base al traffico presente sulle strade interessate.
Il sistema non prevede un apparato centralizzato, ma utilizza un sistema condiviso e standard fra tutti gli utenti.

## Implementazione
Il sistema utilizzerà il progetto OSS [Unity Traffic Simulation](https://github.com/mchrbn/unity-traffic-simulation), basato sul motore grafico Unity, per visualizzare graficamente il movimento del traffico in tempo reale e lo scambio di informazioni fra i veicoli che si incrociano sulla strada.

## Idee per utilizzare i concetti di Icon
* Logica proposizionale che mantiene le regole basilari della strada e che determina la precedenza delle auto agli incroci
* Belief Network per diagnosticare le cause del traffico per una determinata strada (incidenti, lavori in corso, ostacoli sulla carreggiata), prendendo informazioni dagli altri veicoli, quali numero di auto sulla strada, velocità media delle auto, velocità istantanea, etc... (è necessario un training per l'apprendimento)
* Far sapere alle alle altre auto l'intenzione di dove si vuole andare, per evitare che il sistema stesso causi ingorghi dovuti allo scegliere la stessa strada da tutte le auto che ricevono l'informazione 
* Deep Learning per i semafori
* KB in Prolog, condivisione della KB fra le macchine + semplice formula per calcolare i pesi

## Idee per la KB in Prolog
1. La KB ha solo il grafo orientato delle strade, e, per ogni strada, i pesi associati, ovvero tempo di percorrenza, auto incrociate su quella strada, velocità media e timestamp dell'informazione.     
Durante la marcia, quando due macchine si incrociano si scambiano la KB, e se si ricevono dati più aggiornati, vengono integrati nella propria KB. Se si ricevono dati aggiornati, inoltre, viene ricalcolato A-Star per laa destinazione da raggiungere. Durante A-Star, il programma interroga la KB Prolog per conoscere le strade adiacenti e i pesi di ognuna. 
N.B.: data la mole di macchine incontrate, sarebbe meglio implementare un limite/ottimizzazione sullo scambio delle infomazioni, in modo da non rendere il programma troppo pesante.
Es: Utilizzare una sola auto campione che deve raggiungere una destinazione (calcolando A-Star), mentre le altre vagano solo per la mappa raccogliendo dati.
2. Semplificazione di 1: Mantere i pesi nell'ambiente Unity e integrarle in Prolog soltanto nel momento in cui si ricevono informazioni più recenti.
3. Semplificazione ulteriore: utilizzare Prolog soltanto per il grafo. (Non ha molto senso);

## TODO
* Creare nomi casuali per le macchine (Consigliati nomi di Pirati) www.name-generator.org.uk
* Associare Strade con gli Incroci
* Creare una rappresentazione in formato nodo-arco per strade e incroci
* Creare nuovi scenari

## Knowledge Base Car
* fgd
* mhf
* jhf

## Possibili rappresentazioni dei dati
1
```
Mappa di adiacenza:
{
 id_strada: lista_strade_adiacenti(percorribili), id_incrocio
}

Lista incroci:
{
 id_incrocio: tipo_incrocio, lista_strade_precedenza
}

Pesi delle strade:
{
 id_strada: peso_strada
}
```

2
```
 [
     id_incrocio_partenza, id_strada, peso_strada, id_incrocio_arrivo
 ]

 {
     id_incrocio: tipo_incrocio, lista_strade
 }
```

