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


  void mostrar()
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
      rect(_x[i], _y[i], tam - i * 2.5, tam - i * 2.5);
    }
    //rect(mx, my, tam, tam);

  }


  void mover(int pix)
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
      if (abs(dify) > 1.0)
      {
        my = my + dify/15.0;
      }

      if (abs(difx) > 1.0)
      {
        mx = mx + difx/30.0;
      }
    }
  }
