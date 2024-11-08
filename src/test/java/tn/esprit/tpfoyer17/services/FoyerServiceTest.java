package tn.esprit.tpfoyer17.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer17.entities.Foyer;
import tn.esprit.tpfoyer17.entities.Universite;
import tn.esprit.tpfoyer17.repositories.FoyerRepository;
import tn.esprit.tpfoyer17.repositories.UniversiteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FoyerServiceTest {

    @Mock
    private FoyerRepository foyerRepository;

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private FoyerService foyerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddFoyer() {
        Foyer mockFoyer = new Foyer();
        when(foyerRepository.save(any(Foyer.class))).thenReturn(mockFoyer);

        Foyer result = foyerService.addFoyer(mockFoyer);

        assertEquals(mockFoyer, result);
        verify(foyerRepository, times(1)).save(mockFoyer);
    }

    @Test
    public void testGetAllFoyers() {
        List<Foyer> mockFoyers = new ArrayList<>();
        when(foyerRepository.findAll()).thenReturn(mockFoyers);

        List<Foyer> result = foyerService.getAllFoyers();

        assertEquals(mockFoyers, result);
        verify(foyerRepository, times(1)).findAll();
    }

    @Test
    public void testGetFoyerById() {
        long idFoyer = 1L;
        Foyer mockFoyer = new Foyer();
        when(foyerRepository.findById(idFoyer)).thenReturn(Optional.of(mockFoyer));

        Foyer result = foyerService.getFoyerById(idFoyer);

        assertEquals(mockFoyer, result);
        verify(foyerRepository, times(1)).findById(idFoyer);
    }

    @Test
    public void testGetFoyerById_NotFound() {
        long idFoyer = 1L;
        when(foyerRepository.findById(idFoyer)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            foyerService.getFoyerById(idFoyer);
        });

        assertEquals("Foyer not found with id: " + idFoyer, exception.getMessage());
        verify(foyerRepository, times(1)).findById(idFoyer);
    }

    @Test
    public void testDeleteFoyer() {
        long idFoyer = 1L;

        foyerService.deleteFoyer(idFoyer);

        verify(foyerRepository, times(1)).deleteById(idFoyer);
    }

    @Test
    public void testUpdateFoyer() {
        Foyer mockFoyer = new Foyer();
        when(foyerRepository.save(mockFoyer)).thenReturn(mockFoyer);

        Foyer result = foyerService.updateFoyer(mockFoyer);

        assertEquals(mockFoyer, result);
        verify(foyerRepository, times(1)).save(mockFoyer);
    }

    @Test
    public void testAjouterFoyerEtAffecterAUniversite() {
        Foyer mockFoyer = new Foyer();
        Universite mockUniversite = new Universite();
        long idUniversite = 1L;

        when(foyerRepository.save(any(Foyer.class))).thenReturn(mockFoyer);
        when(universiteRepository.findById(idUniversite)).thenReturn(Optional.of(mockUniversite));

        Foyer result = foyerService.ajouterFoyerEtAffecterAUniversite(mockFoyer, idUniversite);

        assertEquals(mockFoyer, result);
        verify(universiteRepository, times(1)).findById(idUniversite);
        verify(universiteRepository, times(1)).save(mockUniversite);
    }

    @Test
    public void testAjouterFoyerEtAffecterAUniversite_UniversiteNotFound() {
        Foyer mockFoyer = new Foyer();
        long idUniversite = 1L;

        when(foyerRepository.save(any(Foyer.class))).thenReturn(mockFoyer);
        when(universiteRepository.findById(idUniversite)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            foyerService.ajouterFoyerEtAffecterAUniversite(mockFoyer, idUniversite);
        });

        assertEquals("Universite with ID " + idUniversite + " not found", exception.getMessage());
        verify(universiteRepository, times(1)).findById(idUniversite);
    }
}
