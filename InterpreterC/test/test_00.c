input j, z;
output i;
i = j;
while(!(z == 0)) {
    i = i + z;
    if(0 < z)
        z = z - 1;
    if(z < 0)
        z = z + 1;
}
