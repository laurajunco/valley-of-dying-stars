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

  void mostrar()
  {
    if (existe)
    {
      image(star, x, y, tam, tam);
    }

  }

  void mover()
  {
    float r = random( 0, 3);


      x += r * sin(angulo);
      y += r * cos(angulo);

      x = constrain(x ,20, width - 20);
      y = constrain(y, 70, height - 70);
      angulo+= 0.01;

  }

  void revisarVida(float xp, float yp)
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
