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

  void mostrar()
  {
    for (Bola temp : bolas)
    {
      temp.mostrar();
    }
  }

  void mover()
  {
    for (Bola temp : bolas)
    {
      temp.mover();
    }
  }

  void revisarVida(float xp, float yp)
  {
    for (Bola temp : bolas)
    {
      temp.revisarVida(xp, yp);
    }
  }

  int cuantasBolas()
  {
    return bolas.length;
  }


  int darMuertas()
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
