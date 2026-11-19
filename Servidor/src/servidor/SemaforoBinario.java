/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

public class SemaforoBinario {
	protected int contador = 0;

	public SemaforoBinario (int valorInicial) {
		contador = valorInicial;
	}

	synchronized public void WAIT () {
		while (contador == 0)
			try {
				wait();
			}
			catch (Exception e) {}
		contador--;
	}

	synchronized public void SIGNAL () {
		contador = 1;
		notify();
	}
}