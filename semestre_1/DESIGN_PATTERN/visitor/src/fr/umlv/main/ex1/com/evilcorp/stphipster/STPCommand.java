package fr.umlv.main.ex1.com.evilcorp.stphipster;

public sealed interface STPCommand permits ElapsedTimeCmd, StartTimerCmd, HelloCmd, StopTimerCmd {
}
