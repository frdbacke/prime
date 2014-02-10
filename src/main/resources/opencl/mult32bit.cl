#pragma OPENCL EXTENSION cl_amd_printf: enable

#define ORIGIN_LENGTH 11

uint GetIndex(const uint16 a, uint i)
{
	uint8 b = select(a.s01234567,a.s89abcdef,(uint8)(select(0U,0xFFFFFFFFU,i&8)));
	uint4 c = select(b.s0123    ,b.s4567    ,(uint4)(select(0U,0xFFFFFFFFU,i&4)));
	uint2 d = select(c.s01      ,c.s23      ,(uint2)(select(0U,0xFFFFFFFFU,i&2)));
	uint  e = select(d.s0       ,d.s1       ,       (select(0U,0xFFFFFFFFU,i&1)));
	return e;
}

uint16 SetIndex(const uint a, uint i)
{
	uint16 ret;
	i&=15;
	ret.s0 = select(0U,a,i==0x0);
	ret.s1 = select(0U,a,i==0x1);
	ret.s2 = select(0U,a,i==0x2);
	ret.s3 = select(0U,a,i==0x3);
	ret.s4 = select(0U,a,i==0x4);
	ret.s5 = select(0U,a,i==0x5);
	ret.s6 = select(0U,a,i==0x6);
	ret.s7 = select(0U,a,i==0x7);
	ret.s8 = select(0U,a,i==0x8);
	ret.s9 = select(0U,a,i==0x9);
	ret.sa = select(0U,a,i==0xa);
	ret.sb = select(0U,a,i==0xb);
	ret.sc = select(0U,a,i==0xc);
	ret.sd = select(0U,a,i==0xd);
	ret.se = select(0U,a,i==0xe);
	ret.sf = select(0U,a,i==0xf);
	return ret;
}

uint16 multwith32bitnumber(uint16 a,  uint b)
{
	ulong overflow=0;
	uint16 ret=0;
#pragma unroll
	for(uint i=0; i<15 ; ++i)
	{
		overflow += ((ulong)(GetIndex(a,i)))*b;
		ret |= SetIndex((uint)overflow,i);
		overflow >>= 32;
	}
	ret |= SetIndex((uint)overflow,15);
	return ret;
	
}

__kernel void multwith32bitnumber_kernel(uint16 a, uint b, __global uint16* result) {
	*result = multwith32bitnumber(a, b);
}

#define MSDPTR_L(n_l)\
    ((n_l) + DIGITS_L (n_l))

#define DIGITS_L(n_l)\
    ((unsigned short)*(n_l))

#define LSDPTR_L(n_l)\
    ((n_l) + 1)
    
#define SETDIGITS_L(n_l, l)\
    (*(n_l) = (unsigned short)(l))

#define BITPERDGT       16UL
    

__kernel void mult (ushort* aa_l, ushort* bb_l, ushort* p_l) /* Allow for double length result    */
{
  register ushort *cptr_l, *bptr_l;
  ushort *a_l, *b_l, *aptr_l, *csptr_l, *msdptra_l, *msdptrb_l;
  ushort av;
  ulong carry;

  if (DIGITS_L (aa_l) < DIGITS_L (bb_l))
    {
      a_l = bb_l;
      b_l = aa_l;
    }
  else
    {
      a_l = aa_l;
      b_l = bb_l;
    }

  msdptra_l = MSDPTR_L (a_l);
  msdptrb_l = MSDPTR_L (b_l);

  carry = 0;
  av = *LSDPTR_L (a_l);
  for (bptr_l = LSDPTR_L (b_l), cptr_l = LSDPTR_L (p_l); bptr_l <= msdptrb_l; bptr_l++, cptr_l++)
    {
      *cptr_l = (ushort)(carry = (ulong)av * (ulong)*bptr_l +
                           (ulong)(ushort)(carry >> BITPERDGT));
    }
  *cptr_l = (ushort)(carry >> BITPERDGT);

  for (csptr_l = LSDPTR_L (p_l) + 1, aptr_l = LSDPTR_L (a_l) + 1; aptr_l <= msdptra_l; csptr_l++, aptr_l++)
    {
      carry = 0;
      av = *aptr_l;
      for (bptr_l = LSDPTR_L (b_l), cptr_l = csptr_l; bptr_l <= msdptrb_l; bptr_l++, cptr_l++)
        {
          *cptr_l = (ushort)(carry = (ulong)av * (ulong)*bptr_l +
              (ulong)*cptr_l + (ulong)(ushort)(carry >> BITPERDGT));
        }
      *cptr_l = (ushort)(carry >> BITPERDGT);
    }

  SETDIGITS_L (p_l, DIGITS_L (a_l) + DIGITS_L (b_l));

}

