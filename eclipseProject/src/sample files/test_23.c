input z,h;
output z;

while(!(h==0))
  {
    if(z>0)
      {
	if(z>300)
	  z=z-520;

	if(!(z>300))
	  z=z+z;
      }

    if(!(z>0))
      {
	if(z>(0-300))
	  z=z+z;

	if(!(z>(0-300)))
	  z=z+500;
      }

    h=h-1;
  }
