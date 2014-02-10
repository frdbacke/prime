#pragma OPENCL EXTENSION cl_amd_printf: enable

#define WORKSIZE 1
/**************************************************************/
/* Constants referring to the internal CLINT-representation   */
/**************************************************************/

#define BASE            0x10000UL
#define BASEMINONE      0xffffU
#define BASEMINONEL     0xffffUL
#define DBASEMINONE     0xffffffffUL
#define BASEDIV2        0x8000U
#define DBASEDIV2       0x80000000U
#define BITPERDGT       16UL
#define LDBITPERDGT     4U


/*******************************************************/
/* Number of digits of CLINT-ojects to base 0x10000    */

#define CLINTMAXDIGIT   256U
/*******************************************************/

#define CLINTMAXSHORT   (CLINTMAXDIGIT + 1)
#define CLINTMAXLONG    ((CLINTMAXDIGIT >> 1) + 1)
#define CLINTMAXBYTE    (CLINTMAXSHORT << 1)
#define CLINTMAXBIT     (CLINTMAXDIGIT << 4)

#define MSDPTR_L(n_l)\
    ((n_l) + DIGITS_L (n_l))

#define DIGITS_L(n_l)\
    ((unsigned short)*(n_l))

typedef ushort clint;

__kernel void cpy_l(__global const clint* src_l, __global clint*  dest_l)
{
  __global clint* lastsrc_l = MSDPTR_L (src_l);

	*dest_l = *src_l;
  while ((*lastsrc_l == 0) && (*dest_l > 0))
    {
      --lastsrc_l;
      --*dest_l;
    }

  while (src_l < lastsrc_l)
    {
      *++dest_l = *++src_l;
    }
    
}
