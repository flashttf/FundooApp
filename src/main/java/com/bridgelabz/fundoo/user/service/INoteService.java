package com.bridgelabz.fundoo.user.service;

import java.util.List;

import com.bridgelabz.fundoo.user.dto.NoteDto;
import com.bridgelabz.fundoo.user.model.Response;

public interface INoteService {
	Response createNote(NoteDto noteDto,String token);
	Response updateNote(NoteDto noteDto,String token,String noteId);
	Response deleteNote(String token,String noteId);
	List<NoteDto> read(String token);
	Response setArchive(String token,String noteId);
	Response setTrash(String token,String noteId);
	Response setPin(String token,String noteId);
	Response addLabelToNote(String noteId,String token,String labelId);
	Response removeLabelFromNote(String noteId,String token,String labelId);
}
