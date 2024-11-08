package tn.esprit.tpfoyer17.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer17.entities.*;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.repositories.BlocRepository;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;
import tn.esprit.tpfoyer17.repositories.ReservationRepository;

import java.time.Year;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllReservations() {
        List<Reservation> mockReservations = new ArrayList<>();
        when(reservationRepository.findAll()).thenReturn(mockReservations);

        List<Reservation> result = reservationService.getAllReservations();

        assertEquals(mockReservations, result);
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    public void testGetReservationById() {
        String idReservation = "123";
        Reservation mockReservation = new Reservation();
        when(reservationRepository.findById(idReservation)).thenReturn(Optional.of(mockReservation));

        Reservation result = reservationService.getReservationById(idReservation);

        assertEquals(mockReservation, result);
        verify(reservationRepository, times(1)).findById(idReservation);
    }

    @Test
    public void testGetReservationById_NotFound() {
        String idReservation = "1";
        when(reservationRepository.findById(idReservation)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            reservationService.getReservationById(idReservation);
        });

        assertEquals("Reservation not found with id: " + idReservation, exception.getMessage());
        verify(reservationRepository, times(1)).findById(idReservation);
    }

    @Test
    public void testDeleteReservation() {
        String idReservation = "123";

        reservationService.deleteReservation(idReservation);

        verify(reservationRepository, times(1)).deleteById(idReservation);
    }

    @Test
    public void testUpdateReservation() {
        Reservation reservation = new Reservation();
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.updateReservation(reservation);

        assertEquals(reservation, result);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @ParameterizedTest
    @ValueSource(strings = { "TRIPLE", "DOUBLE", "SIMPLE" })
    public void testAjouterReservationNew(String typeChambre) {
        long idBloc = 1L;
        long cinEtudiant = 12345678L;
        Chambre mockChambre = new Chambre();
        mockChambre.setTypeChambre(TypeChambre.valueOf(typeChambre));
        Bloc mockBloc = new Bloc();

        when(reservationRepository.findForReservation(idBloc)).thenReturn(null);
        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(new Etudiant());
        when(chambreRepository.getForReservation(idBloc)).thenReturn(mockChambre);
        when(blocRepository.findById(idBloc)).thenReturn(Optional.of(mockBloc));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(new Reservation());

        Reservation result = reservationService.ajouterReservation(idBloc, cinEtudiant);

        assertNotNull(result);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @ParameterizedTest
    @CsvSource({
            "TRIPLE, 3, false",  // Should trigger the condition where size > 1 and EstValide is set to false
            "TRIPLE, 2, false",
            "DOUBLE, 2, false",
            "DOUBLE, 1, false",
            "SIMPLE, 2, false",
            "SIMPLE, 1, false"
    })
    public void testAjouterReservationExisting(String typeChambre, int studentCount, boolean expectedValid) {
        long idBloc = 1L;
        long cinEtudiant = 12345678L;

        Reservation mockExistingReservation = new Reservation();
        Set<Etudiant> etudiants = new HashSet<>();
        for (int i = 0; i < studentCount; i++) {
            etudiants.add(new Etudiant());
        }
        mockExistingReservation.setEtudiants(etudiants);

        Chambre mockChambre = new Chambre();
        mockChambre.setTypeChambre(TypeChambre.valueOf(typeChambre));

        when(reservationRepository.findForReservation(idBloc)).thenReturn(mockExistingReservation);
        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(new Etudiant());
        when(chambreRepository.findByReservationsIdReservation(mockExistingReservation.getIdReservation()))
                .thenReturn(mockChambre);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(mockExistingReservation);

        Reservation result = reservationService.ajouterReservation(idBloc, cinEtudiant);

        assertNotNull(result);

        assertEquals(expectedValid, result.isEstValide());

        if (studentCount > 1) {
            assertFalse(result.isEstValide());
        }

        verify(reservationRepository, times(1)).findForReservation(idBloc);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }


    @Test
    public void testAnnulerReservation() {
        long cinEtudiant = 12345678L;
        Reservation mockReservation = new Reservation();
        mockReservation.setEtudiants(new HashSet<>());
        Etudiant mockEtudiant = new Etudiant();

        when(reservationRepository.findByEtudiantsCinEtudiantAndAnneeUniversitaire(cinEtudiant, Year.now().getValue()))
                .thenReturn(mockReservation);
        when(etudiantRepository.findByCinEtudiant(cinEtudiant)).thenReturn(mockEtudiant);
        when(reservationRepository.save(mockReservation)).thenReturn(mockReservation);

        Reservation result = reservationService.annulerReservation(cinEtudiant);

        assertEquals(mockReservation, result);
        verify(reservationRepository, times(1)).save(mockReservation);
    }

    @Test
    public void testGetReservationParAnneeUniversitaireEtNomUniversite() {
        Date anneeUniversite = new GregorianCalendar(2023, Calendar.JANUARY, 1).getTime();
        String nomUniversite = "Test University";
        List<Reservation> mockReservations = new ArrayList<>();

        when(reservationRepository.findByAnneeUniversitaire_YearAndNomUnuiversite(2023, nomUniversite))
                .thenReturn(mockReservations);

        List<Reservation> result = reservationService
                .getReservationParAnneeUniversitaireEtNomUniversite(anneeUniversite, nomUniversite);

        assertEquals(mockReservations, result);
        verify(reservationRepository, times(1)).findByAnneeUniversitaire_YearAndNomUnuiversite(2023, nomUniversite);
    }
}
