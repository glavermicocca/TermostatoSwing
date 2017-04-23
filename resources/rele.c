/*
 * blink.c:
 *      blinks the first LED
 *      Gordon Henderson, projects@drogon.net
 */
 
#include <wiringPi.h>
#include <stdlib.h>
#include <stdio.h>

int main (int argc, char *argv[])
{
  if(argc != 3)
  {
    printf ("Mancano i parametri GPIO_PIN e LIVELLO 0 o 1\n") ;
    return 1 ;
  }

int PIN = atoi(argv[1]);
int LIVELLO = atoi(argv[2]);
 
  if (wiringPiSetup () == -1)
    return 1 ;
 
  pinMode (PIN, OUTPUT) ;         // aka BCM_GPIO pin 17
 
  digitalWrite (PIN, LIVELLO) ;       // On

  printf ("Welcome\n Set PIN %d to %d\n", PIN, LIVELLO) ;

  return 0 ;
}

