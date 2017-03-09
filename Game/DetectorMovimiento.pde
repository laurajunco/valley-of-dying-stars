import processing.video.*;

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
}
