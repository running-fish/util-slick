package org.sazabi.util.slick.bijection

import com.twitter.bijection.{ImplicitBijection, Injection}

import slick.lifted.{BaseTypeMapper, MappedTypeMapper, TypeMapper}

trait TypeMappers {
  def Bijection2TypeMapper[A, B](implicit bij: ImplicitBijection[A, B],
    tm: TypeMapper[B]): BaseTypeMapper[A] =
      MappedTypeMapper.base[A, B](bij, bij.invert)

  def Injection2TypeMapper[A, B](implicit inj: Injection[A, B],
    tm: TypeMapper[B]): BaseTypeMapper[A] =
      MappedTypeMapper.base[A, B](inj, inj.invert(_).get)
}

object TypeMappers extends TypeMappers
