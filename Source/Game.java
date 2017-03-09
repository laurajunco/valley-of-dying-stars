import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.video.*; 
import ddf.minim.*; 
import ddf.minim.effects.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Game extends PApplet {






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


//mirar a ver como se hace
Capture video;
PImage anterior; //imagen anterior
boolean primeraCaptura = true;
int tol = 25;


public void settings()
{
  size(1280, 800, P3D);
}

public void setup()
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

public void draw()
{
  //background(252, 84, 97);
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


public void jugar()
{
  //println(nivel);
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

  public int sacarPromedio()
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

public int contarPixeles()
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
/*  if(contador > 0)
  {
  println(contador);

}*/
  return contador;
}

public void keyPressed()
{
  if (estado == INICIO)
  {
    estado = JUGAR;
    tiempo = millis()/1000;
  }
  else if (estado == SCORE)
  {
    //todo esto deberia ir en un metodo inicializador
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
  public void captureEvent(Capture video)
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
class Bola
{
  float x, y;
  float tam;
  boolean existe = true;
  PImage star;
  float angulo;

  public Bola()
  {

    x = random(width);
    y = random (200, height - 200);
    tam = 50 + (random (-10, 10));
    star = loadImage("star.png");
    angulo = random (0, 4);
    //existe = true;
  }

  public void mostrar()
  {
    if (existe)
    {
      image(star, x, y, tam, tam);
    }

  }

  public void mover()
  {
    float r = random( 0, 3);


      x += r * sin(angulo);
      y += r * cos(angulo);

      x = constrain(x ,20, width - 20);
      y = constrain(y, 70, height - 70);
      angulo+= 0.01f;

  }

  public void revisarVida(float xp, float yp)
  {
    if ( dist(xp, yp, x, y) < 40 )
    {
      existe = false;
      if(!existe)
      {
        //melodia minim
        ding.rewind();
        ding.play();
      }

    }

  }
}
class Bolas
{
  Bola bolas[];
  int total;

  public Bolas(int cuantas)
  {
    bolas = new Bola[cuantas];
    for (int i = 0; i < cuantas; i++)
    {
      bolas[i] = new Bola();
    }
  }

  public void mostrar()
  {
    for (Bola temp : bolas)
    {
      temp.mostrar();
    }
  }

  public void mover()
  {
    for (Bola temp : bolas)
    {
      temp.mover();
    }
  }

  public void revisarVida(float xp, float yp)
  {
    for (Bola temp : bolas)
    {
      temp.revisarVida(xp, yp);
    }
  }

  public int cuantasBolas()
  {
    return bolas.length;
  }


  public int darMuertas()
  {
    int muertas = 0;
    for (Bola temp : bolas)
    {
      if ( !temp.existe)
      {
        muertas++;
      }
    }
    return muertas;
  }

}
class Cuadro
{
float x;
float y;
float my, mx; //mov suavizado
int tam;
int col;
//DetectorMovimiento dec; //falta crearlo

int num = 12;
float[] _x = new float[num];
float[] _y = new float[num];

Cuadro(){

  x = 0;
  mx = 0;
  tam = 30;
  my = height - tam/2 - 38;
  y = height - tam/2 - 38;
  col = color(255);
  //dec = new DetectorMovimiento();
  }


  public void mostrar()
  {

    noStroke();

    fill(col, 200);

    for (int i = num-1; i > 0; i--) {
    _x[i] = _x[i-1];
    _y[i] = _y[i-1];
    }

    _x[0] = mx;
    _y[0] = my;

    for (int i = 0; i < num; i++) {


    rect(_x[i], _y[i], tam - i*2.5f, tam - i*2.5f);
    }
    //rect(mx, my, tam, tam);

  }


  public void mover(int pix)
  {
    float dify = y - my;
    float difx = x - mx;
    float pixMap = map(pix, 200, 2000, height + tam/2 + 40, 0);
    float pixMap2 = map(pix, 200, 2000, 0, 20);
    pixMap2 = constrain (pixMap2, 0, 20);
    x += pixMap2 + 1;

    if( mx > width + tam/2)
    {
      mx = -tam*2;
      x = -tam*2;
    }
    //println(x);

    //y
      if(pixMap < my + 40)
      {
        y = pixMap;

      }
       if( y <= height + tam/2)
      {
        y += 100 ;
      }

      y = constrain (y , 0, height - tam/2 - 38);

      //suavizados
      if (abs(dify) > 1.0f)
      {
        my = my + dify/15.0f;
      }

      if (abs(difx) > 1.0f)
      {
        mx = mx + difx/30.0f;
      }
    }
  }
/*import processing.video.*;

class DetectorMovimiento{

Capture video;
PImage anterior; //imagen anterior

boolean primeraCaptura = true;

DetectorMovimiento()
{
  //video = vid;
  //crea la imagen anterior (vacia)
  //anterior = createImage(640, 480, RGB);
}

int contarPixeles() {
  int contador = 0;
  anterior.loadPixels();
  video.loadPixels();

  for ( int x = 0; x < video.width; x++ ){
    for ( int y = 0; y < video.height; y++ )
    {
      int loc = x + y * video.width;

      if ( abs(brightness(video.pixels[loc]) - brightness(anterior.pixels[loc])) > 100 )
      {
        contador++;
      }
    }
  }
  return contador;
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






}*/
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Game" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
