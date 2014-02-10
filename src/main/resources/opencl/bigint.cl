#pragma OPENCL EXTENSION cl_amd_printf: enable

#define MSDPTR_L(n_l)\
    ((n_l) + DIGITS_L (n_l))

#define DIGITS_L(n_l)\
    ((unsigned short)*(n_l))

#define LSDPTR_L(n_l)\
    ((n_l) + 1)
    
#define SETDIGITS_L(n_l, l)\
    (*(n_l) = (unsigned short)(l))

void u2clint_l (ushort* num_l, ushort u)
{
  *LSDPTR_L (num_l) = u;
  SETDIGITS_L (num_l, 1);
  
}


#define SETONE_L(n_l)\
    (u2clint_l ((n_l), 1U))

#define BITPERDGT       16UL
#define BASE            0x10000UL
#define BASEMINONE      0xffffU
#define BASEMINONEL     0xffffUL
#define DBASEMINONE     0xffffffffUL
#define BASEDIV2        0x8000U
#define DBASEDIV2       0x80000000U
#define LDBITPERDGT     4U

#define CLINTMAXDIGIT   256U
    

void cpy_l (ushort* dest_l, ushort* src_l)
{
  ushort *lastsrc_l = MSDPTR_L (src_l);
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

    
void setzero(ushort* a) {
	for (int i = 0; i < 65; i++) {
		a[i] = 0;
	}
}
    
void setzero_N(ushort* a, uint length) {
	for (int i = 0; i < length; i++) {
		a[i] = 0;
	}
}
    
void cpyLocal(__global ushort* aGlobal, ushort* aLocal) {
  for (int i = 0; i < 65; i++) {
	 aLocal[i] = aGlobal[i];
  }
}

void cpyGlobal(ushort* localVar, __global ushort* globalVar) {
  for (int i = 0; i < 65; i++) {
	 globalVar[i] = localVar[i];
  }
}

unsigned int ld_l (ushort* n_l)
{
  unsigned int l;
  ushort test;

  l = (unsigned int)DIGITS_L (n_l);
  while (n_l[l] == 0 && l > 0)
    {
      --l;
    }

  if (l == 0)
    {
      return 0;
    }

  test = n_l[l];
  l <<= LDBITPERDGT;

  while ((test & BASEDIV2) == 0)
    {
      test <<= 1;
      --l;
    }

  return l;
}


/*
 THIS FUNCTION SEEMS TO HAVE A PROBLEM WHEN THE D2 IS SINGLE DIGITS NUMBER
 IT IS SPECIALLY TREATED IN THE ORIGINAL SOURCE CODE.

*/
void div (ushort* d1_l, ushort* d2_l, ushort* quot_l, ushort* rem_l)
{
  ushort *rptr_l, *bptr_l;
  ushort b_l[65];
  setzero(b_l);
  ushort r_l[2 + (CLINTMAXDIGIT << 1)]; /* Provide space for double long */
                                       /* dividend + 1 digit */
  setzero_N(r_l, 2 + (CLINTMAXDIGIT << 1));
  ushort *qptr_l, *msdptrb_l, *msdptrr_l, *lsdptrr_l;
  ushort qhat, ri, ri_1, ri_2, bn_1, bn_2;
  ulong right, left, rhat, borrow, carry, sbitsminusd;
  unsigned int d = 0;

  cpy_l (r_l, d1_l);
  cpy_l (b_l, d2_l);

  /* Step 1 */
  msdptrb_l = MSDPTR_L (b_l);

  bn_1 = *msdptrb_l;
  while (bn_1 < BASEDIV2)
    {
      d++;
      bn_1 <<= 1;
    }

  sbitsminusd = (int)BITPERDGT - d;

  if (d > 0)
    {
      bn_1 += *(msdptrb_l - 1) >> sbitsminusd;

      if (DIGITS_L (b_l) > 2)
        {
          bn_2 = (ushort)((*(msdptrb_l - 1) << d) + (*(msdptrb_l - 2) >> sbitsminusd));
        }
      else
        {
          bn_2 = (ushort)(*(msdptrb_l - 1) << d);
        }
    }
  else
    {
      bn_2 = (ushort)(*(msdptrb_l - 1));
    }

  /* Steps 2 and 3 */
  msdptrr_l = MSDPTR_L (r_l) + 1;
  lsdptrr_l = MSDPTR_L (r_l) - DIGITS_L (b_l) + 1;
  *msdptrr_l = 0;

  qptr_l = quot_l + DIGITS_L (r_l) - DIGITS_L (b_l) + 1;

  /* Step 4 */
  while (lsdptrr_l >= LSDPTR_L (r_l))
    {
      ri = (ushort)((*msdptrr_l << d) + (*(msdptrr_l - 1) >> sbitsminusd));

      ri_1 = (ushort)((*(msdptrr_l - 1) << d) + (*(msdptrr_l - 2) >> sbitsminusd));

      if (msdptrr_l - 3 > r_l)
        {
          ri_2 = (ushort)((*(msdptrr_l - 2) << d) + (*(msdptrr_l - 3) >> sbitsminusd));
        }
      else
        {
          ri_2 = (ushort)(*(msdptrr_l - 2) << d);
        }

      if (ri != bn_1)               /* almost always */
        {
          qhat = (ushort)((rhat = ((ulong)ri << BITPERDGT) + (ulong)ri_1) / bn_1);
          right = ((rhat = (rhat - (ulong)bn_1 * qhat)) << BITPERDGT) + ri_2;

          /* test qhat */

          if ((left = (ulong)bn_2 * qhat) > right)
            {
              qhat--;
              if ((rhat + bn_1) < BASE)
                  /* else bn_2 * qhat < rhat * b_l */
                {
                  if ((left - bn_2) > (right + ((ulong)bn_1 << BITPERDGT)))
                    {
                      qhat--;
                    }
                }
            }
        }
      else                        /* ri == bn_1, almost never */
        {
          qhat = BASEMINONE;
          right = ((ulong)(rhat = (ulong)bn_1 + (ulong)ri_1) << BITPERDGT) + ri_2;
          if (rhat < BASE)       /* else bn_2 * qhat < rhat * b_l */
            {
              /* test qhat */

              if ((left = (ulong)bn_2 * qhat) > right)
                {
                  qhat--;
                  if ((rhat + bn_1) < BASE)
                      /* else bn_2 * qhat < rhat * b_l */
                    {
                      if ((left - bn_2) > (right + ((ulong)bn_1 << BITPERDGT)))
                        {
                          qhat--;
                        }
                    }
                }
            }
        }

      /* Step 5 */
      borrow = BASE;
      carry = 0;
      for (bptr_l = LSDPTR_L (b_l), rptr_l = lsdptrr_l; bptr_l <= msdptrb_l; bptr_l++, rptr_l++)
        {
          if (borrow >= BASE)
            {
              *rptr_l = (ushort)(borrow = ((ulong)(*rptr_l) + BASE -
                         (ulong)(ushort)(carry = (ulong)(*bptr_l) *
                         qhat + (ulong)(ushort)(carry >> BITPERDGT))));
            }
          else
            {
              *rptr_l = (ushort)(borrow = ((ulong)(*rptr_l) + BASEMINONEL -
                                (ulong)(ushort)(carry = (ulong)(*bptr_l) *
                                qhat + (ulong)(ushort)(carry >> BITPERDGT))));
            }
        }

      if (borrow >= BASE)
        {
          *rptr_l = (ushort)(borrow = ((ulong)(*rptr_l) + BASE -
                             (ulong)(ushort)(carry >> BITPERDGT)));
        }
      else
        {
          *rptr_l = (ushort)(borrow = ((ulong)(*rptr_l) + BASEMINONEL -
                                    (ulong)(ushort)(carry >> BITPERDGT)));
        }

      /* Step 6 */
      *qptr_l = qhat;

      if (borrow < BASE)
        {
          carry = 0;
          for (bptr_l = LSDPTR_L (b_l), rptr_l = lsdptrr_l; bptr_l <= msdptrb_l; bptr_l++, rptr_l++)
            {
              *rptr_l = (ushort)(carry = ((ulong)(*rptr_l) + (ulong)(*bptr_l) +
                                          (ulong)(ushort)(carry >> BITPERDGT)));
            }
          *rptr_l += (ushort)(carry >> BITPERDGT);
          (*qptr_l)--;
        }

      /* Step 7 */
      msdptrr_l--;
      lsdptrr_l--;
      qptr_l--;
    }

  /* Step 8 */
  SETDIGITS_L (quot_l, DIGITS_L (r_l) - DIGITS_L (b_l) + 1);

  SETDIGITS_L (r_l, DIGITS_L (b_l));

  cpy_l (rem_l, r_l);
}

__kernel void div_krnl (__global ushort* d1_global,__global  ushort* d2_global,__global  ushort* quot_global,__global  ushort* rem_global) {

  ushort d1_l[65];
  setzero(d1_l);
  
  ushort d2_l[65];
  setzero(d2_l);
  
  ushort quot_l[65];
  setzero(quot_l);
  
  ushort rem_l[65];
  setzero(rem_l);
    
  cpyLocal(d1_global, d1_l);
  cpyLocal(d2_global, d2_l);

  div(d1_l, d2_l, quot_l, rem_l);
       
  cpyGlobal(quot_l, quot_global);
  cpyGlobal(rem_l, rem_global);

}

void mod_l (ushort* dv_l, ushort* ds_l, ushort* r_l)
{
  ushort junk_l[110];
  setzero_N(junk_l,110);

  div (dv_l, ds_l, junk_l, r_l);
  
}



void mult ( ushort* aa_l, ushort* bb_l,  ushort* p_l) /* Allow for double length result    */
{
  ushort *cptr_l, *bptr_l;
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


__kernel void mult_krnl (__global ushort* aa_global, __global ushort* bb_global, __global ushort* pp_global) /* Allow for double length result    */
{
  
  ushort p_l[65];
  setzero(p_l);
  ushort aa_l[65];
  setzero(aa_l);
  ushort bb_l[65];
  setzero(bb_l);
  
  cpyLocal(aa_global, aa_l);
  cpyLocal(bb_global, bb_l);

	mult(aa_l, bb_l, p_l);
     
  cpyGlobal(p_l, pp_global);
}




void sub (ushort* a_l, ushort*  b_l, ushort*  d_l)
{

  ushort *msdptra_l, *msdptrb_l;
  ushort *aptr_l = LSDPTR_L (a_l), *bptr_l = LSDPTR_L (b_l), *dptr_l = LSDPTR_L (d_l);
  ulong carry = 0L;

  msdptra_l = MSDPTR_L (a_l);
  msdptrb_l = MSDPTR_L (b_l);

  SETDIGITS_L (d_l, DIGITS_L (a_l));

  while (bptr_l <= msdptrb_l)
    {
      *dptr_l++ = (ushort)(carry = (ulong)*aptr_l++ - (ulong)*bptr_l++
                                         - ((carry & BASE) >> BITPERDGT));
    }

  while (aptr_l <= msdptra_l)
    {
      *dptr_l++ = (ushort)(carry = (ulong)*aptr_l++
                     - ((carry & BASE) >> BITPERDGT));
    }

}


/******************************************************************************/
/*                                                                            */
/*  Function:  Subtraction kernel function                                    */
/*             w/o overflow detection, w/o checking for leading zeros         */
/*  Syntax:    void sub (CLINT a_l, CLINT b_l, CLINT d_l);                    */
/*  Input:     a_l, b_l (Operands)                                            */
/*  Output:    d_l (Difference)                                               */
/*  Returns:   -                                                              */
/*                                                                            */
/******************************************************************************/
__kernel void 
sub_krnl (__global ushort* a_global, __global ushort*  b_global, __global ushort*  d_global)
{
  ushort d_l[65];
  setzero(d_l);
  ushort a_l[65];
  setzero(a_l);
  ushort b_l[65];
  setzero(b_l);
	
  cpyLocal(a_global, a_l);
  cpyLocal(b_global, b_l);

	sub(a_l, b_l, d_l);

	cpyGlobal(d_l, d_global);
}

void msqr_l (ushort* aa_l, ushort* c_l, ushort* m_l)
{
  ushort a_l[65];
  setzero(a_l);

  ushort tmp_l[110];
  setzero_N(tmp_l,110);

  cpy_l (a_l, aa_l);

  // TODO : replace by sqr function
  mult (a_l, a_l, tmp_l);
  mod_l (tmp_l, m_l, c_l);

}

void umul (ushort* a_l, ushort b, ushort* p_l)   /* Allow for double length result     */
{
  ushort *aptr_l, *cptr_l;
  ushort *msdptra_l;
  ulong carry;

  msdptra_l = MSDPTR_L (a_l);

  carry = 0;
  for (aptr_l = LSDPTR_L (a_l), cptr_l = LSDPTR_L (p_l); aptr_l <= msdptra_l; aptr_l++, cptr_l++)
    {
      *cptr_l = (ushort)(carry = (ulong)b * (ulong)*aptr_l +
                          (ulong)(ushort)(carry >> BITPERDGT));
    }
  *cptr_l = (ushort)(carry >> BITPERDGT);

  SETDIGITS_L (p_l, DIGITS_L (a_l) + 1);

}



void ummul_l (ushort* a_l, ushort b, ushort* c_l, ushort* m_l)
{
  ushort tmp_l[110];
  setzero_N(tmp_l,110);

  umul (a_l, b, tmp_l);
  mod_l (tmp_l, m_l, c_l);

}


void wmexp_l (ushort bas, ushort*  e_l, ushort* rem_l, ushort* m_l)
{

  ushort p_l[65];
  setzero(p_l);
  ushort z_l[65];
  setzero(z_l);

  ushort k, b, w;

  cpy_l (z_l, e_l);
  SETONE_L (p_l);

  b = 1 << ((ld_l (z_l) - 1) & (BITPERDGT - 1UL));
  w = *MSDPTR_L (z_l);

  for (; b > 0; b >>= 1)
    {
      msqr_l (p_l, p_l, m_l);
      if ((w & b) > 0)
        {
          ummul_l (p_l, bas, p_l, m_l);
        }
    }

  for (k = DIGITS_L (z_l) - 1; k > 0; k--)
    {
      w = z_l[k];
      for (b = BASEDIV2; b > 0; b >>= 1)
        {
          msqr_l (p_l, p_l, m_l);
          if ((w & b) > 0)
            {
              ummul_l (p_l, bas, p_l, m_l);
            }
        }
    }

  cpy_l (rem_l, p_l);

}


__kernel void wmexp_l_krnl (ushort bas, __global ushort*  e_global, __global ushort* rem_global, __global ushort* m_global)
{
  ushort e_l[65];
  setzero(e_l);
  ushort rem_l[65];
  setzero(rem_l);
  ushort m_l[65];
  setzero(m_l);
	
  cpyLocal(e_global, e_l);
  cpyLocal(m_global, m_l);

  wmexp_l (bas, e_l, rem_l,  m_l);

	cpyGlobal(rem_l, rem_global);
	
}
