package com.example.domain

sealed trait Player
object Player {
  final case object Computer extends Player
  final case object Human    extends Player
}
