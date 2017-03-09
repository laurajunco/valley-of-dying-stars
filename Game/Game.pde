//Laura Catalina Junco Gómez
//Processing 3.3
//valley of dying stars
//March 8th 2017
//Estudo 6: Interacción
//Universidad de los Andes
//Bogotá, Colombia

//librerias
import processing.video.*;
import ddf.minim.*;
import ddf.minim.effects.*;


//estados del juego
final int INICIO = 0;
final int JUGAR = 1;
final int SCORE = 2;
final int GANAR = 3;


//variables de estado y nivel
int estado = INICIO;
int nivel = 0;
int limiteNiveles = 10;

//fuentes
PFont letraTitulo;
PFont letraSub;

//imagenes
PImage fondo;

//variable que guarda al cuadro jugador
Cuadro jugador;
Bolas bits;
int numBolas = 1;

//tiempo
int tiempo = 0;
int limiteTiempo = 30;
int tiempoRestante = 0;
int score = 0;

//minim
Minim minim;
AudioPlayer ding;
AudioPlayer music;


//Captura de video 
//TODO: revisar
Capture video;
PImage anterior; //imagen anterior
boolean primeraCaptura = true;
int tol = 25;

//settings
public void settings()
{
  size(1280, 800, P3D);
}

void setup()
{
  background(0);
  textAlign(CENTER);
  rectMode(CENTER);
  imageMode(CENTER);

  letraTitulo = loadFont("AdelleSans-Bold-200.vlw");
  letraSub = loadFont("AdelleSans-Heavy-50.vlw");
  textFont(letraSub, 18);

  jugador = new Cuadro();
  bits = new Bolas(numBolas);

  video = new Capture(this , 640, 480);
  video.start();
  anterior = createImage(640, 480, RGB);

  minim = new Minim(this);
  ding = minim.loadFile("ding.wav", 2048);
  music = minim.loadFile("music.wav", 2048);

  fondo = loadImage("fondo.png");

  music.loop();

}

void draw()
{

  noStroke();
  image(fondo, width/2, height/2 -50);
  fill(252, 84, 97);
  rect(width/2, height - 20, width, 60);



  //estado inicio
  if (estado == INICIO)
  {

    fill(240);
    textFont(letraTitulo, 100);
    text("EMPEZAR", width/2, height/2 + 25 );
    textFont(letraSub, 18);
    text("Presione una tecla para iniciar", width/2, height/2 + 50 );

  }

  //estado jugar
  if (estado == JUGAR)
  {

    fill(255);
    textFont(letraTitulo, 30);

    //tiempo
    text(tiempoRestante, width/2, height - 10);
    text( score, width - 40, 40);
    jugar();

    if ((millis()/1000 - tiempo)> limiteTiempo)
    {
      estado = SCORE;
    }

    
    fill(255);
    text("Nivel " + (nivel + 1), 60,  35 );

    //cambio de nivel 
    if(bits.cuantasBolas() == bits.darMuertas())
    {

      nivel++;
      numBolas++;
      limiteTiempo += 10;


      if(nivel > limiteNiveles)
      {
        estado = SCORE;

      }

      jugador = new Cuadro();
      bits = new Bolas(numBolas);


    }
  }


  if (estado == SCORE)
    {
      fill(240);

     textFont(letraTitulo, 100);
     text("SCORE : " + score, width/2, height/2 + 25 );
     textFont(letraSub, 18);
     text("Presione una tecla para volver a jugar", width/2, height/2 + 50 );
    }

  }


//estado jugar
void jugar()
{

  jugador.mostrar();
  jugador.mover(sacarPromedio());

  bits.mostrar();
  if (nivel > 3)
  {
    bits.mover();
  }
  bits.revisarVida(jugador.mx, jugador.my);
  score = nivel*(nivel+1)/2  + bits.darMuertas();
  tiempoRestante = limiteTiempo + (tiempo - millis()/1000) + score;

}

  int sacarPromedio()
{
  int promedio = 0;
  for(int i = 0; i < 5; i++)
  {
    promedio += contarPixeles();
  }
  if(promedio > 0)
  {
  //println(promedio);
  }
  return promedio/5;
}

int contarPixeles()
{
  int contador = 0;
  anterior.loadPixels();
  video.loadPixels();

  for ( int x = 0; x < video.width; x++ ){
    for ( int y = 0; y < video.height; y++ )
    {
      int loc = x + y * video.width;

      if ( abs(brightness(video.pixels[loc]) - brightness(anterior.pixels[loc])) > tol )
      {
        contador++;

      }
    }
  }
  return contador;
}


//cambio de estados para iniciar y finalizar
void keyPressed()
{
  if (estado == INICIO)
  {
    estado = JUGAR;
    tiempo = millis()/1000;
  }
  else if (estado == SCORE)
  {
    //todo esto deberia ir en un metodo inicializador setup??
    estado = INICIO;
    score = 0;
    nivel = 0;
    numBolas = 1;
    limiteTiempo = 30;
    jugador = new Cuadro();
    bits = new Bolas(numBolas);
  }

}

//se activa cuando haya un new frame disponible
  void captureEvent(Capture video)
  {
      if(primeraCaptura)
      {
        println("empezamos");
        primeraCaptura = false;
      } else  {
      //antes de leer una nueva imagen, guarda la actual en anterior
      anterior.copy(video, 0, 0, video.width, video.height, 0, 0, anterior.width, anterior.height);
      anterior.updatePixels();
      video.read();
      }
  }
