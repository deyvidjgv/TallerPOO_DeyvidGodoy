package co.edu.puj.taller7.modelo;

// contrato: todo lo que pelea en la arena tiene que saber defenderse,
// cada clase decide cuanto dano le quita su armadura o su escudo
public interface ICombatiente {
    void defender(int danoRecibido);
}
