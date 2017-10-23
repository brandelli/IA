(define (domain agente)
 (:requirements :strips) 
 (:predicates (Em ?loc) (Sala ?loc) (Corredor ?loc) (Deposito ?loc) (TemEscada ?esc) (TemLampadas ?lamp) (Lampada ?lamp) (Escada ?esc) (Queimada ?loc) (Funcionando ?loc) (SobreEscada ?esc)) 

 (:action entrarComodo :parameters (?loc1 ?loc2) 
  :precondition (and (Em ?loc1) (Corredor ?loc1) (or (Deposito ?loc2) (Sala ?loc2)))
  :effect (and (Em ?loc2) (not (Em ?loc1))))
  
 (:action sairComodo :parameters (?loc1 ?loc2 ?esc) 
  :precondition (and (Em ?loc1) (or (Deposito ?loc1) (Sala ?loc1)) (Corredor ?loc2) (Escada ?esc) (not(SobreEscada ?esc)))
  :effect (and (Em ?loc2) (not (Em ?loc1)) ))
  
 (:action pegarEscada :parameters (?loc ?esc) 
  :precondition (and (Em ?loc) (Deposito ?loc) (not(TemEscada ?esc)) (Escada ?esc))
  :effect (TemEscada ?esc))
 
 (:action pegarLampadas :parameters (?loc ?lamp) 
  :precondition (and (Em ?loc) (Deposito ?loc) (not(TemLampadas ?lamp)) (Lampada ?lamp))
  :effect (TemLampadas ?lamp))
  
 (:action trocarLampada :parameters (?loc ?lamp ?esc) 
  :precondition (and (Em ?loc) (Sala ?loc) (TemLampadas ?lamp) (Lampada ?lamp) (TemEscada ?esc) (Escada ?esc) (Queimada ?loc) (SobreEscada ?esc))
  :effect (and(not(Queimada ?loc)) (Funcionando ?loc)))
  
 (:action subirEscada :parameters (?loc ?esc) 
  :precondition (and (Em ?loc) (Sala ?loc) (TemEscada ?esc) (Escada ?esc) (not(SobreEscada ?esc)))
  :effect (SobreEscada ?esc))
  
 (:action descerEscada :parameters (?loc ?esc) 
  :precondition (and (Em ?loc) (Sala ?loc) (TemEscada ?esc) (Escada ?esc) (SobreEscada ?esc))
  :effect (not(SobreEscada ?esc)))

)