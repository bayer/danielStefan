input a, b;

output c;


if( a > b )
{
    c = a - b;
}

if( !(a > b) )
{
    c = b - a;
}

assert( !(c < 0) );
